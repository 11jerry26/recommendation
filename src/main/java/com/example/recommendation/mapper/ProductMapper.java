package com.example.recommendation.mapper;

import com.example.recommendation.entity.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface ProductMapper {
    //分页查询商品列表
    public List<Product> getProductByPage(int offset, int pageSize, int userId, String searchInput);

    //根据分类分页查询商品列表
    public List<Product> getProductByCategory(int offset, int pageSize, int userId,int categoryId);

    //根据商品id查询商品
    public Product getProductInfoById(int productId);

    //查询总商品数
    public int selectProductTotalCount();

    //根据商品id列表查询商品列表
    public List<Product> selectProductsByIds(@Param("projectIds") List<Integer> projectIds);

    //查询商品分类总数
    public int selectCategoryCount();

    /**
     * 批量查询商品ID对应的分类ID
     * @param productIds 商品ID列表
     * @return 一个Map，key是商品ID，value是分类ID
     */
    List<Map<String, Object>> selectCategoryIdByProductIds(List<Integer> productIds);

    //查询所有商品
    public List<Product> selectAllProducts();
}
