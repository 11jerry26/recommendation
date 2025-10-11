package com.example.recommendation.mapper;

import com.example.recommendation.entity.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProductMapper {
    //分页查询商品列表
    public List<Product> getProductByPage(int offset, int pageSize, int userId);

    //根据分类分页查询商品列表
    public List<Product> getProductByCategory(int offset, int pageSize, int userId,int categoryId);

    //根据商品id查询商品
    public Product getProductInfoById(int productId);

    //查询总商品数
    public int selectProductTotalCount();

    //根据商品id列表查询商品列表
    public List<Product> selectProductsByIds(@Param("projectIds") List<Integer> projectIds);
}
