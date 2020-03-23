package com.imooc.o2o.util;

/**
 * @description:图片路径获取
 * @author: zhangyihan
 * @createDate: 2020-03-11 16:01
 * @version: 1.0
 */
public class PathUtil {
    private static String seperator = System.getProperty("file.separator");//获得系统的分隔符
    /**
     * 返回项目图片的根路径
     * @return
     */
    public static String getImgBasePath() {
        String os = System.getProperty("os.name");//os.name是固定参数，表示获取系统名称
        String basePath = "";
        if(os.toLowerCase().startsWith("win")) {
            basePath = "D:/project/image/";
        } else {
            basePath = "/Users/shawn/Document/实战练习/image/";
        }
        basePath = basePath.replace("/", seperator);//用新值替换旧值，确保该路径在不同系统下都是有效的
        return basePath;
    }
    /**
     * 获取店铺图片的相对子路径（将图片分别存储在各自店铺的路径下），因此传入shopId加以区分
     * @return
     */
    public static String getShopImagePath(long shopId) {
        String imagePath = "upload/item/shop/"+shopId+"/";
        return imagePath.replace("/", seperator);//用新值替换旧值，确保该路径在不同系统下都是有效的
    }
}