package com.imooc.o2o.util;

/**
 * @description:
 * @author: zhangyihan
 * @createDate: 2020-03-18 15:58
 * @version: 1.0
 */
public class PageCalculator {
    public static int calculateRowIndex(int pageIndex, int pageSize) {
        return (pageIndex > 0) ? (pageIndex - 1) * pageSize : 0;

    }
}