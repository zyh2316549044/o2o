package com.imooc.o2o.dao;

import com.imooc.o2o.BaseTest;
import com.imooc.o2o.dto.ShopExecution;
import com.imooc.o2o.entity.Area;
import com.imooc.o2o.entity.PersonInfo;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.entity.ShopCategory;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * @description:
 * @author: zhangyihan
 * @createDate: 2020-03-10 14:14
 * @version: 1.0
 */
public class ShopDaoTest extends BaseTest {

    @Autowired

    private ShopDao shopDao;

    @Test
    public void testQueryShopList(){
        Shop shop = new Shop();
        PersonInfo ownwer = new PersonInfo();
        ownwer.setUserId(8L);
        shop.setOwner(ownwer);
        List<Shop> shops = shopDao.queryShopList(shop, 0, 5);
        System.out.println(shops.size());

    }




    @Test
    public void testInsertShop(){
        Shop shop = new Shop();
        PersonInfo personInfo = new PersonInfo();
        Area area = new Area();
        ShopCategory shopCategory = new ShopCategory();
        personInfo.setUserId(8L);
        area.setAreaId(3L);
        shopCategory.setShopCategoryId(10L);
        shop.setArea(area);
        shop.setOwner(personInfo);
        shop.setShopName("测试店铺");
        shop.setShopDesc("test");
        shop.setShopAddr("test");
        shop.setPhone("test");
        shop.setShopImg("test");
        shop.setCreateTime(new Date());
        shop.setEnableStatus(1);
        shop.setAdvice("审核中");
        int effnum = shopDao.insertShop(shop);
        assertEquals(1,effnum);

    }

    @Test
    public void testUpdateShop(){
        Shop shop = new Shop();
        shop.setShopId(30L);
        shop.setShopAddr("aaa");
        shop.setPhone("bbb");
        shop.setCreateTime(new Date());
        int effnum = shopDao.updateShop(shop);
        assertEquals(1,effnum);

    }

    @Test
    public void testqueryShop(){
        long shopId = 15;
        Shop shop = shopDao.queryShopId(shopId);
        System.out.println(shop.getArea().getAreaName());//根据Area表查询到AreaName表数据
    }
}
