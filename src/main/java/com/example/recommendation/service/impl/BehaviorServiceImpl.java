package com.example.recommendation.service.impl;

import com.example.recommendation.entity.CartProduct;
import com.example.recommendation.entity.Order;
import com.example.recommendation.entity.Product;
import com.example.recommendation.entity.UserBehavior;
import com.example.recommendation.entity.request.AddCartDTO;
import com.example.recommendation.entity.request.PurchaseDTO;
import com.example.recommendation.mapper.BehaviorMapper;
import com.example.recommendation.mapper.ProductMapper;
import com.example.recommendation.service.BehaviorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class BehaviorServiceImpl implements BehaviorService {
    @Autowired
    BehaviorMapper behaviorMapper;
    @Autowired
    ProductMapper productMapper;

    @Override
    public boolean browseProduct(UserBehavior userBehavior) {
        int userId = userBehavior.getUserId();
        int productId = userBehavior.getProductId();
        int behaviorTypeId = userBehavior.getBehaviorTypeId();
        String behaviorTime = userBehavior.getBehaviorTime();
        int isDelete = userBehavior.getIsDelete();
        int result = behaviorMapper.browseProduct(userId,productId,behaviorTypeId,behaviorTime,isDelete);
        return result == 1;
    }

    @Override
    public boolean collectProduct(UserBehavior userBehavior) {
        int userId = userBehavior.getUserId();
        int productId = userBehavior.getProductId();
        int behaviorTypeId = userBehavior.getBehaviorTypeId();
        String behaviorTime = userBehavior.getBehaviorTime();
        int isDelete = userBehavior.getIsDelete();
        int isExist = behaviorMapper.searchCollectCount(userId,productId,behaviorTypeId);
        int result = 0;
        if (isExist == 0) {
            result = behaviorMapper.insertCollect(userId,productId,behaviorTypeId,behaviorTime,0);
        } else {
            result = behaviorMapper.updateCollect(userId,productId,behaviorTypeId,behaviorTime,isDelete);
        }
        return result == 1;
    }

    @Override
    public boolean searchCollectStatus(UserBehavior userBehavior) {
        int userId = userBehavior.getUserId();
        int productId = userBehavior.getProductId();
        int behaviorTypeId = userBehavior.getBehaviorTypeId();
        int count = behaviorMapper.searchCollectCount(userId,productId,behaviorTypeId);
        if (count == 0) {
            return false;
        } else {
            UserBehavior record = behaviorMapper.searchCollect(userId,productId,behaviorTypeId);
            if (record.getIsDelete() == 1) {
                return false;
            } else {
                return true;
            }
        }
    }

    @Override
    public int addCart(AddCartDTO addCartDTO) {
        int userId = addCartDTO.getUserId();
        int productId = addCartDTO.getProductId();
        int count = addCartDTO.getCount();
        int isExist = behaviorMapper.searchCartProduct(userId,productId);
        int result = 0;
        if (isExist > 0) {
            //如果购物车表已有数据，就只加count
            result = behaviorMapper.addCartProductCount(userId,productId,count);
        } else {
            //否则新加一条数据
            result = behaviorMapper.addCart(userId,productId,count);
        }
        return result;
    }

    @Override
    public int addCartBehavior(AddCartDTO addCartDTO) {
        int userId = addCartDTO.getUserId();
        int productId = addCartDTO.getProductId();
        int behaviorTypeId = addCartDTO.getBehaviorTypeId();
        String behaviorTime = addCartDTO.getBehaviorTime();
        int isDelete = addCartDTO.getIsDelete();
        int isExist = behaviorMapper.searchCartBehavior(userId,productId,behaviorTypeId);
        int result = 0;
        if (isExist > 0) {
            //如果行为表中已添加过这个商品，那么更新行为时间到最新
            result = behaviorMapper.updateCartBehavior(userId,productId,behaviorTypeId,behaviorTime,isDelete);
        } else {
            //否则新增行为
            result = behaviorMapper.addCartBehavior(userId,productId,behaviorTypeId,behaviorTime,isDelete);
        }
        return result;
    }

    @Override
    public CartProduct[] getAllCartProduct(UserBehavior userBehavior) {
        int userId = userBehavior.getUserId();
        return behaviorMapper.getAllCartProduct(userId);
    }

    @Override
    public int changeCartProductCount(AddCartDTO addCartDTO) {
        int userId = addCartDTO.getUserId();
        int productId = addCartDTO.getProductId();
        int cartCount = addCartDTO.getCount();
        int result = behaviorMapper.changeCartProductCount(userId,productId,cartCount);
        return result;
    }

    @Override
    public int deleteCartProduct(UserBehavior userBehavior) {
        int userId = userBehavior.getUserId();
        int productId = userBehavior.getProductId();
        int behaviorTypeId = userBehavior.getBehaviorTypeId();
        String behaviorTime = userBehavior.getBehaviorTime();
        int result1 = behaviorMapper.updateCartBehavior(userId,productId,behaviorTypeId,behaviorTime,1);
        int result2 = behaviorMapper.deleteCartProduct(userId,productId);
        return result1 + result2;
    }

    @Override
    public int purchaseCartProduct(PurchaseDTO[] purchaseDTOS) {
        for (PurchaseDTO dto : purchaseDTOS) {
            int userId = dto.getUserId();
            int productId = dto.getProductId();
            int sellCount = dto.getSellCount();
            int cartCount = dto.getCartCount();
            Product product = productMapper.getProductInfoById(productId);
            double price = product.getPrice();
            double totalPrice = cartCount * price;
            String behaviorTime = dto.getBehaviorTime();


            //清除购物车表
            int deleteCart = behaviorMapper.deleteCartProduct(userId,productId);
            //修改该商品销量
            int updateProductSellCount = behaviorMapper.updateProductSellCount(productId,sellCount + cartCount);
            //新增购物行为
            int addPurchaseBehavior = behaviorMapper.addCartBehavior(userId,productId,4,behaviorTime,0);
            //新增订单记录
            int addOrder = behaviorMapper.insertOrder(userId,productId,cartCount,totalPrice,behaviorTime,0);
        }
        return 0;
    }

    @Override
    public Order[] selectUserOrder(UserBehavior userBehavior) {
        int userId = userBehavior.getUserId();
        int isDelete = userBehavior.getIsDelete();
        List<Order> orderList = behaviorMapper.selectUserOrders(userId, isDelete);
        Order[] orders = orderList.toArray(new Order[0]);

        return orders;
    }

    @Override
    public int deleteUserOrder(Order order) {
        int id = order.getId();
        return behaviorMapper.deleteUserOrder(id,1);
    }

    @Override
    public Product[] selectUserCollection(UserBehavior userBehavior) {
        int userId = userBehavior.getUserId();
        int behaviorTypeId = userBehavior.getBehaviorTypeId();
        List<Integer> productIds = behaviorMapper.selectUserCollectProductIds(userId,behaviorTypeId,0);

        if (productIds == null || productIds.isEmpty()) {
            return new Product[0];
        }

        Product[] productArray = new Product[productIds.size()];

        // 遍历ID列表，查询商品对象并填充数组
        for (int i = 0; i < productIds.size(); i++) {
            Integer productId = productIds.get(i);
            Product product = productMapper.getProductInfoById(productId);
            productArray[i] = product;
        }
        return productArray;
    }
}
