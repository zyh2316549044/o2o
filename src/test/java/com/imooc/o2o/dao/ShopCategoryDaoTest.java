package com.imooc.o2o.dao;

import com.imooc.o2o.BaseTest;
import com.imooc.o2o.entity.Area;
import com.imooc.o2o.entity.ShopCategory;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * @description:
 * @author: zhangyihan
 * @createDate: 2020-03-14 11:39
 * @version: 1.0
 */
public class ShopCategoryDaoTest extends BaseTest {

    @Autowired
    private ShopCategoryDao shopCategoryDao;

    @Test
    public void testQueryArea(){
        List<ShopCategory> areas = shopCategoryDao.queryShopCategory(new ShopCategory());
        assertEquals(18,areas.size());//junit测试提供方法：判断返回的方法是否为4条数据
    }


}
