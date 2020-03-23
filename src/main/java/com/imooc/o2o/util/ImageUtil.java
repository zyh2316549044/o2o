package com.imooc.o2o.util;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * @description:图片上传
 * @author: zhangyihan
 * @createDate: 2020-03-11 14:28
 * @version: 1.0
 */

public class ImageUtil {

   private static Logger logger = LoggerFactory.getLogger(ImageUtil.class);

    //获取项目路径下的路径文件
    private static String path = Thread.currentThread().getContextClassLoader().getResource("").getPath();
    private static final SimpleDateFormat sDateFormat = new SimpleDateFormat(
            "yyyyMMddHHmmss"); // 时间格式化的格式
    private static final Random r = new Random();


    /**
     * 将CommonsMultipartFile转换成File
     * @param cFile
     * @return
     */
    public static File transferCommonsMultipartFileToFile(CommonsMultipartFile cFile) {
        File newFile = new File(cFile.getOriginalFilename());
        try {
            cFile.transferTo(newFile);//将cFile的文件流写入到新创建的文件newFile里
        } catch (IllegalStateException e) {
            logger.error(e.toString());
            e.printStackTrace();
        } catch (IOException e) {
            logger.error(e.toString());
            e.printStackTrace();
        }
        return newFile;

    }

    /**
     * 1 处理缩略图: 门面照, 商品小图
     * thumbnail: 文件处理对象
     * targetAddr: 图片的存储路径
     */
    public static String generateThumbnail(InputStream thumbnailInputStream, String fileName,String targetAddr) {
        //1 获得文件的随机名,避免上传的文件名重复
        String realFileName = getRandomFileName();
        //2 获得上传文件的扩展名->后缀
        String extension = getFileExtension(fileName);
        //3 创建图片的存储路径
        makeDirPath(targetAddr);
        //4 文件存储的相对路径
        String relativeAddr = targetAddr + realFileName + extension;

        //5 日志: 相对路径
        logger.debug("current relativeAddr is: " + relativeAddr);

        //6 生成文件的路径: 图片根路径+图片相对路径
        File dest = new File(PathUtil.getImgBasePath() + relativeAddr);
        logger.debug("current complete addr is: " + PathUtil.getImgBasePath() + relativeAddr);
        //7 处理文件对象
        try {
            Thumbnails.of(thumbnailInputStream).size(200, 200)
                    .watermark(Positions.BOTTOM_RIGHT, ImageIO.read(new File(path + "/1.jpg")), 0.25f)
                    .outputQuality(0.5f).toFile(dest);
        } catch (IOException e) {
            logger.error(e.toString());
            e.printStackTrace();
        }
        return relativeAddr;
    }
    /**
     * 生成随机数
     *
     * @return
     */
    public static String getRandomFileName(){
        int rannum = r.nextInt(89999) + 10000;
        String format = sDateFormat.format(new Date());
        return format+rannum;
    }


    /**
     *
     * 获取输入文件流的扩展名
     * @param fileName
     * @return
     */
    private static String getFileExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
    }


    /**
     *
     * 创建目标路经所涉及到的目录
     * @param targetAddr
     */
    private static void makeDirPath(String targetAddr) {
        String realFileParentPath = PathUtil.getImgBasePath() + targetAddr;
        File dirPath = new File(realFileParentPath);
        if (!dirPath.exists()) {
            dirPath.mkdirs();//递归创建路径上所写的一切文件夹
        }
    }
    /**
     * d 删除图片或者文件夹
     */
    public static void deleteFileOrPath(String storePath) {
        File fileOrPath = new File(PathUtil.getImgBasePath() + storePath);
        if (fileOrPath.exists()) {
            if (fileOrPath.isDirectory()) {
                File[] files = fileOrPath.listFiles();
                for (int i = 0; i < files.length; i++) {
                    files[i].delete();
                }
            }
            fileOrPath.delete();
        }
    }

}