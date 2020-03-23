package com.imooc.o2o.service;

import com.imooc.o2o.dto.ShopExecution;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.exceptions.ShopOperationException;
import com.sun.xml.internal.ws.message.saaj.SAAJHeader;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.File;
import java.io.InputStream;

/**
 * @description:店铺
 * @author: zhangyihan
 * @createDate: 2020-03-11 19:38
 * @version: 1.0
 */
public interface ShopService {



    /**
     * 根据shopCondition分页返回相应的店铺列表
     */
    ShopExecution getShopList(Shop shopCondition, Integer pageIndex, Integer pageSize);

    /**
     * 根据店铺id获取店铺信息
     */
    Shop getByShopId(Long shopId);

    /**
     * 更新店铺的信息,包括对图片的处理
     */
    ShopExecution modifyShop(Shop shop, InputStream shopImgInputStream,String fileName) throws ShopOperationException;

    /**
     * 注册商铺
     */
    ShopExecution addShop(Shop shop, InputStream shopImgInputStream,String fileName)throws ShopOperationException;
}
