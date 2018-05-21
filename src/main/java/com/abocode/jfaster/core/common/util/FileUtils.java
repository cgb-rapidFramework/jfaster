package com.abocode.jfaster.core.common.util;

import com.abocode.jfaster.core.common.model.json.DataGrid;
import com.abocode.jfaster.core.common.util.ConvertUtils;
import com.abocode.jfaster.web.system.domain.entity.Icon;

import javax.imageio.stream.FileImageOutputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.List;
import java.util.UUID;

/**
 * 文件操作工具类
 *
 * @author 张代浩
 */
public class FileUtils {

    /**
     * 获取文件扩展名
     *
     * @param filename
     * @return
     */
    public static String getExtend(String filename) {
        return getExtend(filename, "");
    }

    /**
     * 获取文件扩展名
     *
     * @param filename
     * @return
     */
    public static String getExtend(String filename, String defExt) {
        if ((filename != null) && (filename.length() > 0)) {
            int i = filename.lastIndexOf('.');

            if ((i > 0) && (i < (filename.length() - 1))) {
                return (filename.substring(i + 1)).toLowerCase();
            }
        }
        return defExt.toLowerCase();
    }

    /**
     * 获取文件名称[不含后缀名]
     *
     * @param
     * @return String
     */
    public static String getFilePrefix(String fileName) {
        int splitIndex = fileName.lastIndexOf(".");
        return fileName.substring(0, splitIndex).replaceAll("\\s*", "");
    }

    /**
     * 获取文件名称[不含后缀名]
     * 不去掉文件目录的空格
     *
     * @param
     * @return String
     */
    public static String getFilePrefix2(String fileName) {
        int splitIndex = fileName.lastIndexOf(".");
        return fileName.substring(0, splitIndex);
    }
    /**
     * 判断文件是否为图片<br>
     * <br>
     *
     * @param filename 文件名<br>
     *                 判断具体文件类型<br>
     * @return 检查后的结果<br>
     * @throws Exception
     */
    public static boolean isPicture(String filename) {
        // 文件名称为空的场合
        if (ConvertUtils.isEmpty(filename)) {
            // 返回不和合法
            return false;
        }
        // 获得文件后缀名
        //String tmpName = getExtend(filename);
        String tmpName = filename;
        // 声明图片后缀名数组
        String imgeArray[][] = {{"bmp", "0"}, {"dib", "1"},
                {"gif", "2"}, {"jfif", "3"}, {"jpe", "4"},
                {"jpeg", "5"}, {"jpg", "6"}, {"png", "7"},
                {"tif", "8"}, {"tiff", "9"}, {"ico", "10"}};
        // 遍历名称数组
        for (int i = 0; i < imgeArray.length; i++) {
            // 判断单个类型文件的场合
            if (imgeArray[i][0].equals(tmpName.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断文件是否为DWG<br>
     * <br>
     *
     * @param filename 文件名<br>
     *                 判断具体文件类型<br>
     * @return 检查后的结果<br>
     * @throws Exception
     */
    public static boolean isDwg(String filename) {
        // 文件名称为空的场合
        if (ConvertUtils.isEmpty(filename)) {
            // 返回不和合法
            return false;
        }
        // 获得文件后缀名
        String tmpName = getExtend(filename);
        // 声明图片后缀名数组
        if (tmpName.equals("dwg")) {
            return true;
        }
        return false;
    }

    /**
     * 删除指定的文件
     *
     * @param strFileName 指定绝对路径的文件名
     * @return 如果删除成功true否则false
     */
    public static boolean delete(String strFileName) {
        File fileDelete = new File(strFileName);

        if (!fileDelete.exists() || !fileDelete.isFile()) {
            return false;
        }
        return fileDelete.delete();
    }

    /**
     * 把数据库中图片byte，存到项目temp目录下，并且把路径返设置给TsIcon
     * @param dataGrid
     * @param request
     */
    public static void convertDataGrid(DataGrid dataGrid, HttpServletRequest request){
        String fileDirName=request.getSession().getServletContext().getRealPath("")+File.separator+"temp";
        delFolder(fileDirName);
        File fileDir = new File(fileDirName);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }
        try {
            List list=dataGrid.getResults();
            for(Object obj:list){
                Icon icon=(Icon)obj;
                String fileName="icon"+ UUID.randomUUID()+"."+icon.getExtend();
                File tempFile=new File(fileDirName+File.separator+fileName);
                if(icon.getIconContent()!=null){
                    byte2image(icon.getIconContent(),tempFile);
                    icon.setIconPath("temp/"+fileName);
                }
            }
        } catch (Exception e) {
            LogUtils.error(e.getMessage());
        }
    }

    //byte数组到图片
    private static void byte2image(byte[] data,File file){
        if( data.length < 3 ) return;
        FileImageOutputStream imageOutput = null;
        try {
            imageOutput = new FileImageOutputStream(file);
            imageOutput.write(data, 0, data.length);
            imageOutput.close();
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 删除文件夹
     * @param folderPath 文件夹完整绝对路径
     */
    private static void delFolder(String folderPath) {
        try {
            delAllFile(folderPath); //删除完里面所有内容
            File folder = new File(folderPath);
            folder.delete(); //删除空文件夹
        } catch (Exception e) {
            LogUtils.error(e.getMessage());
        }
    }
    /**
     * 删除指定文件夹下所有文件
     * @param path 文件夹完整绝对路径
     * @return
     */
    private static boolean delAllFile(String path) {
        boolean flag = false;
        File file = new File(path);
        if (!file.exists()) {
            return flag;
        }
        if (!file.isDirectory()) {
            return flag;
        }
        String[] tempList = file.list();
        File temp = null;
        for (int i = 0; i < tempList.length; i++) {
            if (path.endsWith(File.separator)) {
                temp = new File(path + tempList[i]);
            } else {
                temp = new File(path + File.separator + tempList[i]);
            }
            if (temp.isFile()) {
                temp.delete();
            }
            if (temp.isDirectory()) {
                delAllFile(path + "/" + tempList[i]);//先删除文件夹里面的文件
                delFolder(path + "/" + tempList[i]);//再删除空文件夹
                flag = true;
            }
        }
        return flag;
    }
}
