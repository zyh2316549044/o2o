package com.imooc.o2o.service;

import com.imooc.o2o.dto.ProdutCategoryExeution;
import com.imooc.o2o.entity.ProductCategory;
import com.imooc.o2o.exceptions.ProductCategoryOperationException;

import java.util.List;

/**
 * @description:
 * @author: zhangyihan
 * @createDate: 2020-03-19 16:35
 * @version: 1.0
 */
public interface ProductCategoryService {
    List<ProductCategory> getProductCategoryList(long shopId);
    ProdutCategoryExeution batchAddProductCategory(List<ProductCategory> productCategoryList) throws ProductCategoryOperationException;

    /**
     *
     * @param productCategoryId
     * @param shopId
     * @return
     * @throws RuntimeException
     */
    ProdutCategoryExeution deleteProductCategory(long productCategoryId,
                                                   long shopId) throws RuntimeException;
}