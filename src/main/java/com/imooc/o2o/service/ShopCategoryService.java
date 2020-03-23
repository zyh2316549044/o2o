package com.imooc.o2o.service;

import com.imooc.o2o.dto.ProdutCategoryExeution;
import com.imooc.o2o.entity.ProductCategory;
import com.imooc.o2o.entity.ShopCategory;
import com.imooc.o2o.exceptions.ProductCategoryOperationException;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @description:
 * @author: zhangyihan
 * @createDate: 2020-03-14 17:24
 * @version: 1.0
 */
@Service
public interface ShopCategoryService {

    List<ShopCategory> queryShopCategory(ShopCategory shopCategoryCondition);


}
