package org.jeecgframework.web.utils;

import org.jeecgframework.platform.util.FileUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by admin on 2016/1/4.
 */
public class ResourceUtils extends FileUtils{
    //定义资源类型
    public final  static String  RESOURCE_TEMPLATE="resource/template";
    public final  static String  RESOURCE_FILE="resource/upload";
    /***
     * 获取图片保存路径
     * @return
     */
   public static  String getDateDir(){
       Date date = new Date();
       String path=new SimpleDateFormat("yyyy/MM/dd").format(date);
       return  path;
   }

    /***
     * 获取资源服务器的路径
     *
     * @return
     */
    public static  String getResourceLocalPath(){
        String  serverPath=ConfigUtils.getConfigByName("resource.server.path");
        return  serverPath;
    }

}