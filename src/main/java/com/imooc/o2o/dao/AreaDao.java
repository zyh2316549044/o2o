package com.imooc.o2o.dao;

import com.imooc.o2o.entity.Area;

import java.util.List;

/**
 * @description:区域性
 * @author: zhangyihan
 * @createDate: 2020-03-09 10:28
 * @version: 1.0
 */
public interface AreaDao {
    /**
     * 列出区域列表
     * @return
     */
    List<Area> queryArea();


}
