package com.imooc.o2o.dao;

import com.imooc.o2o.BaseTest;
import com.imooc.o2o.entity.Area;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * @description:
 * @author: zhangyihan
 * @createDate: 2020-03-09 10:49
 * @version: 1.0
 */
public class AreaDaoTest extends BaseTest {
    
    @Autowired
    private AreaDao areaDao;
    
    @Test
    public void testQueryArea(){
        List<Area> areas = areaDao.queryArea();
        assertEquals(4,areas.size());//junit测试提供方法：判断返回的方法是否为4条数据
    }
    

}
