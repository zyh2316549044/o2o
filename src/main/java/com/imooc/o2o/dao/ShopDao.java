package com.imooc.o2o.dao;

import com.imooc.o2o.entity.Shop;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @description:
 * @author: zhangyihan
 * @createDate: 2020-03-10 13:45
 * @version: 1.0
 */
public interface ShopDao {


    /**
     * 分页查询店铺，可输入的条件有：店铺名(模糊)，店铺状态，店铺类别，区域id，owner
     * @param shopCondition
     * @param rowIndex 从第几行开始取数据
     * @param pageSize 返回的条数
     * @return
     */
    List<Shop> queryShopList(@Param("shopCondition") Shop shopCondition,@Param("rowIndex") int rowIndex,@Param("pageSize") int pageSize);


    /**
     * queryShopList条件查询总数（分页查询出来的数据+分页查询没显示的数据）
     */
    int queryShopCount(@Param("shopCondition") Shop shopCondition);

    /***
     *根据shop id 查询店铺
     *
     */
    Shop queryShopId(long shopId);


    /**
     * 新增店铺
     * @param shop
     * @return
     */
    int insertShop(Shop shop);


    /**
     * 更新店铺
     * @param shop
     * @return
     */
    int updateShop(Shop shop);
}
