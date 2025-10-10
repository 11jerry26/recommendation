package com.example.recommendation.service.impl;

import com.example.recommendation.entity.Product;
import com.example.recommendation.entity.UserBehavior;
import com.example.recommendation.mapper.BehaviorMapper;
import com.example.recommendation.mapper.ProductMapper;
import com.example.recommendation.mapper.UserMapper;
import com.example.recommendation.service.RecommendService;
import org.deeplearning4j.datasets.iterator.utilty.ListDataSetIterator;
import org.deeplearning4j.datasets.iterator.utilty.SingletonMultiDataSetIterator;
import org.deeplearning4j.nn.api.OptimizationAlgorithm;
import org.deeplearning4j.nn.conf.ComputationGraphConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.graph.MergeVertex;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.EmbeddingLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.graph.ComputationGraph;
import org.deeplearning4j.nn.weights.WeightInit;
import org.deeplearning4j.util.ModelSerializer;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.dataset.api.MultiDataSet;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.dataset.api.iterator.MultiDataSetIterator;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.learning.config.Adam;
import org.nd4j.linalg.lossfunctions.LossFunctions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class RecommendServiceImpl implements RecommendService {
    @Autowired
    BehaviorMapper behaviorMapper;
    @Autowired
    ProductMapper productMapper;
    @Autowired
    UserMapper userMapper;

    private static final String MODEL_PATH = "recommendation-model.zip";
    private static final int BATCH_SIZE = 128;
    private static final int EPOCHS = 10;
    private static final int MAX_SEQUENCE_LENGTH = 20; // 用于获取近期行为的数量
    private static final int EMBEDDING_SIZE = 50;

    private ComputationGraph model;
    private int numUsers;
    private int numProducts;

    @PostConstruct
    @Override
    public void initModel() throws IOException {
        // 1. 获取用户和商品的总数，用于构建模型
        numUsers = userMapper.selectUserTotalCount();
        numProducts = productMapper.selectProductTotalCount();
        System.out.println("系统中共有 " + numUsers + " 个用户，" + numProducts + " 个商品。");

        File modelFile = new File(MODEL_PATH);
        if (modelFile.exists()) {
            // 2. 如果模型已存在，直接加载 (注意：这里使用 restoreComputationGraph)
            System.out.println("正在加载已训练好的推荐模型...");
            // 使用 restoreComputationGraph 来加载计算图模型
            model = ModelSerializer.restoreComputationGraph(modelFile);
            System.out.println("模型加载成功！");
        } else {
            // 3. 如果模型不存在，训练一个新模型
            System.out.println("未找到模型，正在使用数据库数据进行训练...");
            // 【重要】你的 DataSetIterator 也需要适配 ComputationGraph
            // 它应该返回一个 DataSet 对象，其中 features 是一个包含两个 INDArray 的数组
            // 我们稍后会讨论如何修改 prepareTrainingData()
            MultiDataSetIterator dataSetIterator = prepareTrainingData();

            // 这里的赋值现在是正确的，因为 model 和 build 方法的返回类型都是 ComputationGraph
            model = buildNCFModelWithComputationGraph();

            model.fit(dataSetIterator, EPOCHS);
            System.out.println("模型训练完成！");

            // 4. 保存训练好的模型 (ComputationGraph 和 MultiLayerNetwork 的保存方法相同)
            ModelSerializer.writeModel(model, modelFile, true);
            System.out.println("模型已保存至: " + modelFile.getAbsolutePath());
        }
    }

    /**
     * 构建神经协同过滤（NCF）模型 (使用 ComputationGraph，无需 MergeLayer)
     */
    private ComputationGraph buildNCFModelWithComputationGraph() {
        // 定义计算图的配置
        ComputationGraphConfiguration.GraphBuilder confBuilder = new NeuralNetConfiguration.Builder()
                .seed(123)
                .optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT)
                .updater(new Adam(0.001))
                .graphBuilder()

                // --- 1. 定义输入 ---
                .addInputs("userInput", "itemInput")

                // --- 2. 定义用户嵌入分支 ---
                .addLayer("userEmbedding", new EmbeddingLayer.Builder()
                                .nIn(numUsers) // 输入维度：用户ID的数量
                                .nOut(EMBEDDING_SIZE) // 输出维度：嵌入向量的大小
                                .weightInit(WeightInit.XAVIER)
                                .build(),
                        "userInput") // 将该层连接到 "userInput" 输入

                // --- 3. 定义商品嵌入分支 ---
                .addLayer("itemEmbedding", new EmbeddingLayer.Builder()
                                .nIn(numProducts) // 输入维度：商品ID的数量
                                .nOut(EMBEDDING_SIZE) // 输出维度：嵌入向量的大小
                                .weightInit(WeightInit.XAVIER)
                                .build(),
                        "itemInput") // 将该层连接到 "itemInput" 输入

                // --- 4. 合并嵌入向量 (核心替代方案) ---
                // 使用 MergeVertex 来拼接两个嵌入层的输出
                // 拼接后的向量长度 = EMBEDDING_SIZE + EMBEDDING_SIZE
                .addVertex("merge", new MergeVertex(), "userEmbedding", "itemEmbedding")

                // --- 5. 定义主网络部分 ---
                .addLayer("dense1", new DenseLayer.Builder()
                                .nIn(EMBEDDING_SIZE * 2) // 输入维度是合并后的向量大小
                                .nOut(64)
                                .activation(Activation.RELU)
                                .weightInit(WeightInit.XAVIER)
                                .build(),
                        "merge") // 将全连接层连接到 "merge" 顶点的输出

                // --- 6. 定义输出层 ---
                .addLayer("output", new OutputLayer.Builder()
                                .nIn(64)
                                .nOut(1)
                                .activation(Activation.SIGMOID)
                                .lossFunction(LossFunctions.LossFunction.MSE)
                                .build(),
                        "dense1") // 将输出层连接到 "dense1" 层

                // --- 7. 定义输出名称 ---
                .setOutputs("output");

        // 构建配置并创建 ComputationGraph
        ComputationGraphConfiguration conf = confBuilder.build();
        return new ComputationGraph(conf);
    }

    /**
     * 准备训练数据
     */
    private MultiDataSetIterator prepareTrainingData() {
        // 1. 获取所有用户最近的行为数据
        List<UserBehavior> allBehaviors = behaviorMapper.selectRecentBehaviors(MAX_SEQUENCE_LENGTH);
        if (allBehaviors == null || allBehaviors.isEmpty()) {
            throw new RuntimeException("数据库中没有找到任何用户行为数据，无法训练模型！");
        }

        int numSamples = allBehaviors.size();
        // 2. 分离输入：创建2个独立的INDArray
        INDArray userIds = Nd4j.create(numSamples, 1);
        INDArray itemIds = Nd4j.create(numSamples, 1);
        INDArray labels = Nd4j.create(numSamples, 1);

        // 3. 填充数据
        for (int i = 0; i < numSamples; i++) {
            UserBehavior behavior = allBehaviors.get(i);
            userIds.putScalar(i, 0, behavior.getUserId() - 1);
            itemIds.putScalar(i, 0, behavior.getProductId() - 1);
            labels.putScalar(i, 0, getWeightByBehaviorType(behavior.getBehaviorTypeId()));
        }

        // 4. 创建MultiDataSet
        MultiDataSet multiDataSet = new org.nd4j.linalg.dataset.MultiDataSet(
                new INDArray[]{userIds, itemIds}, // 输入数组
                new INDArray[]{labels}            // 标签数组
        );

        // 5. 返回MultiDataSetIterator
        return new SingletonMultiDataSetIterator(multiDataSet);
    }

    /**
     * 根据行为类型获取权重
     */
    private double getWeightByBehaviorType(int typeId) {
        switch (typeId) {
            case 4: return 3.0; // 购买
            case 2: return 2.0; // 收藏
            case 3: return 2.0; // 加购
            case 1: return 1.0; // 点击
            default: return 0.5; // 其他行为
        }
    }

    @Override
    public List<Product> getHybridRecommend(Integer userId, Integer pageNum, Integer pageSize) {
        // 1. 检查用户ID是否有效 (假设用户ID从1开始)
        if (userId == null || userId <= 0 || userId > numUsers) {
            // 如果是新用户或无效用户，返回热门商品
            int offset = (pageNum - 1) * pageSize;
            return productMapper.getProductByPage(offset, pageSize, userId);
        }

        // 2. 获取用户已经购买过的商品，避免重复推荐
        Set<Integer> interactedProducts = behaviorMapper.selectProductIdsByUserId(userId)
                .stream()
                .collect(Collectors.toSet());

        // 3. 生成所有候选商品的ID列表 (1-based)
        List<Integer> allProductIds = IntStream.rangeClosed(1, numProducts)
                .boxed()
                .collect(Collectors.toList());

        // 4. 修复：分离输入（用户ID和商品ID分开，不再合并）
        int numCandidates = allProductIds.size();
        INDArray userInputPredict = Nd4j.create(numCandidates, 1); // 用户ID输入
        INDArray itemInputPredict = Nd4j.create(numCandidates, 1); // 商品ID输入

        for (int i = 0; i < numCandidates; i++) {
            userInputPredict.putScalar(i, 0, userId - 1); // 用户ID转0-based
            itemInputPredict.putScalar(i, 0, allProductIds.get(i) - 1); // 商品ID转0-based
        }

        // 5. 用INDArray[]封装输入，顺序与模型输入一致（userInput在前，itemInput在后）
        INDArray[] predictInputs = new INDArray[]{userInputPredict, itemInputPredict};

        // 6. 调用模型预测（传入2个输入，适配模型）
        INDArray[] outputs = model.output(predictInputs);
        INDArray predictions = outputs[0]; // 取出预测分数

        // 7. 后续的分数排序、分页、查询商品逻辑保持不变...
        Map<Integer, Double> productScores = new HashMap<>();
        for (int i = 0; i < allProductIds.size(); i++) {
            int productId = allProductIds.get(i);
            if (!interactedProducts.contains(productId)) {
                double score = predictions.getDouble(i);
                productScores.put(productId, score);
            }
        }

        List<Map.Entry<Integer, Double>> sortedEntries = productScores.entrySet().stream()
                .sorted(Map.Entry.<Integer, Double>comparingByValue().reversed())
                .collect(Collectors.toList());

        List<Integer> sortedProductIds = sortedEntries.stream()
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        int start = (pageNum - 1) * pageSize;
        int end = Math.min(start + pageSize, sortedProductIds.size());
        if (start >= sortedProductIds.size()) {
            return new ArrayList<>();
        }
        List<Integer> pagedProductIds = sortedProductIds.subList(start, end);

        List<Product> products = productMapper.selectProductsByIds(pagedProductIds);
        Map<Integer, Product> productMap = products.stream()
                .collect(Collectors.toMap(Product::getProductId, product -> product));

        List<Product> result = new ArrayList<>();
        for (Integer productId : pagedProductIds) {
            Product product = productMap.get(productId);
            if (product != null) {
                result.add(product);
            }
        }

        return result;
    }
}
