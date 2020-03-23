package com.imooc.o2o.service.impl;

import com.imooc.o2o.dao.ProductCategoryDao;
import com.imooc.o2o.dao.ShopCategoryDao;
import com.imooc.o2o.dto.ProdutCategoryExeution;
import com.imooc.o2o.entity.ProductCategory;
import com.imooc.o2o.entity.ShopCategory;
import com.imooc.o2o.enums.ProductCategoryStateEnum;
import com.imooc.o2o.exceptions.ProductCategoryOperationException;
import com.imooc.o2o.service.ShopCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @description:
 * @author: zhangyihan
 * @createDate: 2020-03-14 17:26
 * @version: 1.0
 */
@Service
public class ShopCategoryServiceImpl implements ShopCategoryService {

    @Autowired
    private ShopCategoryDao shopCategoryDao;

    @Autowired
    private ProductCategoryDao productCategoryDao;

    @Override
    public List<ShopCategory> queryShopCategory(ShopCategory shopCategoryCondition) {
        return shopCategoryDao.queryShopCategory(shopCategoryCondition);
    }

}
