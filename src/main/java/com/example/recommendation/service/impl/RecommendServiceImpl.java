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
        // 每次初始化时都重新获取最新的用户和商品数量
        numUsers = userMapper.selectUserTotalCount();
        numProducts = productMapper.selectProductTotalCount();
        System.out.println("系统中共有 " + numUsers + " 个用户，" + numProducts + " 个商品。");

        File modelFile = new File(MODEL_PATH);
        if (modelFile.exists()) {
            // 检查模型是否与新数据兼容
            ComputationGraph oldModel = ModelSerializer.restoreComputationGraph(modelFile);

            // 修正：通过层配置获取nIn参数
            int oldItemNIn = (int) ((EmbeddingLayer) oldModel.getLayer("itemEmbedding").conf().getLayer()).getNIn();
            int oldUserNIn = (int) ((EmbeddingLayer) oldModel.getLayer("userEmbedding").conf().getLayer()).getNIn();

            if (oldItemNIn == numProducts && oldUserNIn == numUsers) {
                System.out.println("正在加载已训练好的推荐模型...");
                model = oldModel;
                System.out.println("模型加载成功！");
                return;
            }
            System.out.println("模型结构已过期，将重新训练...");
        }

        // 重新训练模型
        System.out.println("正在使用数据库数据进行训练...");
        MultiDataSetIterator dataSetIterator = prepareTrainingData();
        model = buildNCFModelWithComputationGraph();
        model.fit(dataSetIterator, EPOCHS);
        System.out.println("模型训练完成！");
        ModelSerializer.writeModel(model, modelFile, true);
        System.out.println("模型已保存至: " + modelFile.getAbsolutePath());
    }

    /**
     * 构建神经协同过滤（NCF）模型 (使用 ComputationGraph，无需 MergeLayer)
     */
    private ComputationGraph buildNCFModelWithComputationGraph() {
        //商品分类总数
        int numCategories = productMapper.selectCategoryCount();

        // 定义计算图的配置
        ComputationGraphConfiguration.GraphBuilder confBuilder = new NeuralNetConfiguration.Builder()
                .seed(123)
                .optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT)
                .updater(new Adam(0.001))
                .graphBuilder()

                // --- 1. 定义输入 ---
                .addInputs("userInput", "itemInput","categoryInput")

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

                // --- 商品类别嵌入分支 ---
                .addLayer("categoryEmbedding", new EmbeddingLayer.Builder()
                                .nIn(numCategories)
                                .nOut(EMBEDDING_SIZE)
                                .weightInit(WeightInit.XAVIER)
                                .build(),
                        "categoryInput")

                // --- 4. 合并嵌入向量 (核心替代方案) ---
                // 使用 MergeVertex 来拼接两个嵌入层的输出
                // 拼接后的向量长度 = EMBEDDING_SIZE + EMBEDDING_SIZE
                .addVertex("merge", new MergeVertex(), "userEmbedding", "itemEmbedding", "categoryEmbedding")

                // --- 5. 定义主网络部分 ---
                .addLayer("dense1", new DenseLayer.Builder()
                                .nIn(EMBEDDING_SIZE * 2 + EMBEDDING_SIZE) // 输入维度是合并后的向量大小
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
        //提取所有不重复的 productId
        Set<Integer> productIdSet = allBehaviors.stream()
                .map(UserBehavior::getProductId)
                .collect(Collectors.toSet());
        //批量查询这些商品ID对应的分类ID
        List<Map<String, Object>> productCategoryList = productMapper.selectCategoryIdByProductIds(new ArrayList<>(productIdSet));

        // 2. 手动将List转换为Map<Integer, Integer>
        Map<Integer, Integer> productToCategoryMap = new HashMap<>();
        for (Map<String, Object> item : productCategoryList) {
            // MyBatis默认使用列名作为Map的key，这里假设列名为productId和categoryId
            Integer productId = (Integer) item.get("productId");
            Integer categoryId = (Integer) item.get("categoryId");
            if (productId != null && categoryId != null) {
                productToCategoryMap.put(productId, categoryId);
            }
        }

        // 2. 分离输入：创建2个独立的INDArray
        INDArray userIds = Nd4j.create(numSamples, 1);
        INDArray itemIds = Nd4j.create(numSamples, 1);
        INDArray categoryIds = Nd4j.create(numSamples, 1);
        INDArray labels = Nd4j.create(numSamples, 1);

        // 3. 填充数据
        for (int i = 0; i < numSamples; i++) {
            UserBehavior behavior = allBehaviors.get(i);
            int productId = behavior.getProductId();

            userIds.putScalar(i, 0, behavior.getUserId() - 1);
            itemIds.putScalar(i, 0, behavior.getProductId() - 1);
            Integer categoryId = productToCategoryMap.get(productId);
            if (categoryId == null) {
                // 重要：处理找不到分类的情况，比如设置一个默认值或跳过这条数据
                // 这里我们简单地抛出异常，你可以根据业务需求调整
                throw new RuntimeException("商品ID为 " + productId + " 的分类未在数据库中找到！");
            }
            categoryIds.putScalar(i, 0, categoryId - 1);
            labels.putScalar(i, 0, getWeightByBehaviorType(behavior.getBehaviorTypeId()));
        }

        // 4. 创建MultiDataSet
        MultiDataSet multiDataSet = new org.nd4j.linalg.dataset.MultiDataSet(
                new INDArray[]{userIds, itemIds, categoryIds}, // 输入数组
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
            return productMapper.getProductByPage(offset, pageSize, userId,"");
        }

        // 2. 获取用户已经购买过的商品，避免重复推荐
        Set<Integer> interactedProducts = behaviorMapper.selectProductIdsByUserId(userId)
                .stream()
                .collect(Collectors.toSet());

        // 获取所有商品及其类别信息
        List<Product> allProducts = productMapper.selectAllProducts();

        // 3. 生成所有候选商品的ID列表 (1-based)
        List<Integer> allProductIds = IntStream.rangeClosed(1, numProducts)
                .boxed()
                .collect(Collectors.toList());

        // 4. 修复：分离输入（用户ID和商品ID分开，不再合并）
        int numCandidates = allProductIds.size();
        INDArray userInputPredict = Nd4j.create(numCandidates, 1); // 用户ID输入
        INDArray itemInputPredict = Nd4j.create(numCandidates, 1); // 商品ID输入
        INDArray categoryInputPredict = Nd4j.create(numCandidates, 1); // 分类ID输入

        for (int i = 0; i < numCandidates; i++) {
            Product product = allProducts.get(i);
            userInputPredict.putScalar(i, 0, userId - 1); // 用户ID转0-based
            itemInputPredict.putScalar(i, 0, allProductIds.get(i) - 1); // 商品ID转0-based
            categoryInputPredict.putScalar(i, 0, product.getCategoryId() - 1);
        }

        // 5. 用INDArray[]封装输入，顺序与模型输入一致（userInput在前，itemInput在后）
        INDArray[] predictInputs = new INDArray[]{userInputPredict, itemInputPredict, categoryInputPredict};

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
