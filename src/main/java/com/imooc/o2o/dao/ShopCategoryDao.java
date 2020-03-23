package com.imooc.o2o.dao;

import java.util.List;

import com.imooc.o2o.entity.ShopCategory;
import org.apache.ibatis.annotations.Param;


public interface ShopCategoryDao {

	/**
	 * 获得店铺类别
	 */
	List<ShopCategory> queryShopCategory(@Param(value = "shopCategoryCondition") ShopCategory shopCategoryCondition);

}
