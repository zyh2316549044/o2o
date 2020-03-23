package com.imooc.o2o.dao;

import java.util.List;

import com.imooc.o2o.entity.ProductCategory;
import org.apache.ibatis.annotations.Param;

public interface ProductCategoryDao {

	/**
	 * 通过shop id查询店铺的商品类别
	 */
	List<ProductCategory> queryProductCategoryList(long shopId);


	/**
	 *
	 *批量新增商品类别
	 */
	int batchInsertProductCategory(List<ProductCategory> productCategoryList);

	/**
	 * 批量删除商品的类别
	 *
	 */
	int deleteProductCategory(@Param("productCategoryId") Long productCategoryId,@Param("shopId") Long shopId);


}
