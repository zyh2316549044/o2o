package com.imooc.o2o.service.impl;

import com.imooc.o2o.dao.ShopDao;
import com.imooc.o2o.dto.ShopExecution;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.enums.ShopStateEnum;
import com.imooc.o2o.exceptions.ShopOperationException;
import com.imooc.o2o.service.ShopService;
import com.imooc.o2o.util.ImageUtil;
import com.imooc.o2o.util.PageCalculator;
import com.imooc.o2o.util.PathUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.util.Date;
import java.util.List;

/**
 * @description:店铺
 * @author: zhangyihan
 * @createDate: 2020-03-11 19:40
 * @version: 1.0
 */
@Service
public class ShopServiceImpl implements ShopService {

    @Autowired
    private ShopDao shopDao;

    /**
     * 根据shopCondition分页返回店铺列表
     * @param shopCondition
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @Override
    public ShopExecution getShopList(Shop shopCondition, Integer pageIndex, Integer pageSize) {

        // 前端返回的是页数，但Dao层只认行数，因此需要做转换
        int rowIndex = PageCalculator.calculateRowIndex(pageIndex, pageSize);
        List<Shop> shopList = shopDao.queryShopList(shopCondition, rowIndex, pageSize);
        int count = shopDao.queryShopCount(shopCondition);
        ShopExecution se = new ShopExecution();
        if (shopList != null) {
            se.setShopList(shopList);
            se.setCount(count);
        } else {
            se.setState(ShopStateEnum.INNER_ERROR.getState());
        }
        return se;

    }


    /***
     *
     * 根据id查询店铺信息
     * @param shopId
     * @return
     */
    @Override
    public Shop getByShopId(Long shopId) {
        return shopDao.queryShopId(shopId);
    }

    /**
     * 更新店铺的信息,包括对图片的处理
     */
    @Override
    public ShopExecution modifyShop(Shop shop, InputStream shopImgInputStream, String fileName) throws ShopOperationException {

        if (shop != null && shop.getShopId() == null){
            return new ShopExecution(ShopStateEnum.NULL_SHOP_INFO);
        }else {
            try {

                //判断是否需要处理图片
                if (shopImgInputStream != null && fileName != null && !"".equals(fileName)) {
                    Shop tempshop = shopDao.queryShopId(shop.getShopId());
                    if (tempshop.getShopImg() != null) {
                        ImageUtil.deleteFileOrPath(tempshop.getShopImg());
                    }
                    addShopImg(shop, shopImgInputStream, fileName);
                }
                //更新店铺信息
                shop.setLastEditTime(new Date());
                int i = shopDao.updateShop(shop);
                if (i <= 0) {
                    return new ShopExecution(ShopStateEnum.INNER_ERROR);
                } else {
                    shop = shopDao.queryShopId(shop.getShopId());
                    return new ShopExecution(ShopStateEnum.SUCCESS, shop);
                }
            }catch (Exception e){
                throw new ShopOperationException("modeifshop" +e.getMessage());
            }
        }

    }

    /**
     * 添加店铺
     * @param shop
     * @param shopImgInputStream
     * @return
     */
    @Override
    @Transactional//添加事务回滚
    public ShopExecution addShop(Shop shop, InputStream shopImgInputStream, String fileName) {
        //空值判断
        if (shop == null){
            return new ShopExecution();
        }
        try {
            shop.setEnableStatus(0);
            shop.setCreateTime(new Date());
            shop.setLastEditTime(new Date());
            int effectedNum = shopDao.insertShop(shop);
            if (effectedNum <= 0){
                throw new RuntimeException("店铺创建失败");
            }else {
                if (shopImgInputStream != null){
                    //存储图片
                    try {
                        addShopImg(shop,shopImgInputStream,fileName);
                    }catch (Exception e){
                        throw new RuntimeException("addShopImg"+ e.getMessage());
                    }
                    effectedNum = shopDao.updateShop(shop);
                    if (effectedNum <= 0){
                        throw new RuntimeException("更新图片地址失败");
                    }
                }
            }
        }catch (Exception e){
            throw new RuntimeException("addShop error"+ e.getMessage());
        }
        return new ShopExecution(ShopStateEnum.CHECK,shop);
    }

    private void addShopImg(Shop shop, InputStream shopImgInputStream, String fileName) {
        //获取shop图片目录的相对值路径
        String dest= PathUtil.getShopImagePath(shop.getShopId());
        String shopImgAddr = ImageUtil.generateThumbnail(shopImgInputStream,fileName,dest);
        shop.setShopImg(shopImgAddr);
    }

}
