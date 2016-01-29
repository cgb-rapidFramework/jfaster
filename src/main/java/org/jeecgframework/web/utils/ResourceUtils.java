package org.jeecgframework.web.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by admin on 2016/1/4.
 */
public class ResourceUtils {
   public static  String getDateDir(){
       Date date = new Date();
       String path=new SimpleDateFormat("yyyy/MM/dd").format(date);
       return  path;
   }

    public static  String getResourceServerURL(){
        Date date = new Date();
        String path=new SimpleDateFormat("yyyy/MM/dd").format(date);
        return  path;
    }
}
