package com.imooc.o2o.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.acl.Owner;
import java.util.Date;
import java.util.List;

import com.imooc.o2o.BaseTest;
import com.imooc.o2o.dto.ShopExecution;
import com.imooc.o2o.entity.Area;
import com.imooc.o2o.entity.PersonInfo;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.entity.ShopCategory;
import com.imooc.o2o.enums.ShopStateEnum;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.ws.Service;

import static org.junit.Assert.assertEquals;

public class ShopServiceTest extends BaseTest {
	@Autowired
	private ShopService shopService;

	@Test
	public void testModifyShop() throws Exception {
		Shop byShopId = new Shop();
		byShopId.setShopId(15L);
		byShopId.setShopName("修改后");
		File shopImg = new File("E:/2.jpg");
		InputStream is = new FileInputStream(shopImg);
		ShopExecution shopExecution = shopService.modifyShop(byShopId, is, "2.jpg");
		System.out.println("新图片地址"+shopExecution.getShop().getShopImg());
	}
	
	@Test
	public void testAddShop() throws Exception {
		Shop shop = new Shop();
		PersonInfo personInfo = new PersonInfo();
		Area area = new Area();
		ShopCategory shopCategory = new ShopCategory();
		personInfo.setUserId(8L);
		area.setAreaId(3L);
		shopCategory.setShopCategoryId(10L);
		shop.setArea(area);
		shop.setOwner(personInfo);
		shop.setShopName("aa店铺1");
		shop.setShopDesc("test1");
		shop.setShopAddr("test1");
		shop.setPhone("test1");
		shop.setShopImg("test1");
		shop.setCreateTime(new Date());
		shop.setLastEditTime(new Date());
		shop.setEnableStatus(1);
		shop.setAdvice("审核中");

		File shopImg = new File("E:/1.jpg");
		InputStream is = new FileInputStream(shopImg);
		ShopExecution se = shopService.addShop(shop,is,shopImg.getName());
		assertEquals("aa店铺1", se.getShop().getShopName());
	}

	@Test
	public void testGetShopList() {
		Shop shopCondition = new Shop();
		ShopCategory sc = new ShopCategory();
		sc.setShopCategoryId(14L);
		shopCondition.setShopCategory(sc);
		ShopExecution se = shopService.getShopList(shopCondition, 1, 2);//第一页，每页两条数据
		System.out.println("店铺列表数为"+se.getShopList().size());
		System.out.println("店铺总数为"+se.getCount());
	}


}
