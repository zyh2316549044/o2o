package com.imooc.o2o.util;

import javax.servlet.http.HttpServletRequest;

/**
 * @description: HttpSerletRequest装换各种类型值
 * @author: zhangyihan
 * @createDate: 2020-03-12 11:15
 * @version: 1.0
 */
public class HttpServletRequestUtil {

    /**
     *
     * 将request的值取出
     * @param request 请求体
     * @param key 值
     * @return
     */
    public static int getInt(HttpServletRequest request, String key) {
        try {
            return Integer.decode(request.getParameter(key));
        } catch (Exception e) {
            return -1;
        }
    }

    public static Long getLong(HttpServletRequest request, String key) {
        try {
            return Long.valueOf(request.getParameter(key));
        } catch (Exception e) {
            return -1L;
        }
    }

    public static double getDouble(HttpServletRequest request, String key) {
        try {
            return Double.valueOf(request.getParameter(key));
        } catch (Exception e) {
            return -1d;
        }
    }

    public static boolean getBoolean(HttpServletRequest request, String key) {
        try {
            return Boolean.valueOf(request.getParameter(key));
        } catch (Exception e) {
            return false;
        }
    }

    public static String getString(HttpServletRequest request, String key) {
        try {
            String result = request.getParameter(key);
            if (result != null) {
                //去掉左右两边空格
                result = result.trim();
            }
            if ("".equals(result)) {
                return null;
            }
            return result;
        } catch (Exception e) {
            return null;
        }
    }
}
