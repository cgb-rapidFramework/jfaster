package com.abocode.jfaster.core.common.util;

import com.abocode.jfaster.core.common.exception.ContextRuntimeException;
import com.abocode.jfaster.core.repository.DataGridData;
import com.abocode.jfaster.system.entity.Icon;
import lombok.extern.slf4j.Slf4j;

import javax.imageio.stream.FileImageOutputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
public class FileUtils {
    private FileUtils() {
    }
    //定义资源类型
    private static final String  RESOURCE_TEMPLATE="resource/template";
    private static final  String  RESOURCE_FILE="resource/upload";

    public static String getResourceTemplate() {
        return RESOURCE_TEMPLATE;
    }

    public static String getResourceFile() {
        return RESOURCE_FILE;
    }

    /***
     * 创建文件目录如果不存在
     * @param path
     */
    public static boolean mkdirIfNotExists(String path) {
        File dir = new File(path);
        return mkdirIfNotExists(dir);
    }

    public static boolean mkdirIfNotExists(File dir) {
        if (!dir.exists()) {
            return dir.mkdirs();
        }
        return true;
    }

    /***
     * 创建文件目录如果不存在
     * @param path
     */
    public static void deleteIfExists(String path) {
        File file = new File(path);
        try {
            Files.deleteIfExists(file.toPath());
        } catch (IOException e) {
            throw new ContextRuntimeException("删除文件失败", e);
        }
    }

    /***
     * 创建新文件
     * @param file
     * @return
     */
    public static boolean createFileIfNotExists(File file) {
        try {
            boolean res = true;
            if (!file.exists()) {
                res = file.createNewFile();
            }
            return res;
        } catch (IOException e) {
            throw new ContextRuntimeException("创建文件失败", e);
        }
    }

    /***
     * 创建新文件
     * @param file
     * @return
     */
    public static boolean createNewFile(File file) {
        try {
            Files.deleteIfExists(file.toPath());
            return file.createNewFile();
        } catch (IOException e) {
            throw new ContextRuntimeException("创建文件失败", e);
        }
    }

    /***
     * 将字符串写入到文件
     */
    public static void writeToFile(File file, String content,boolean append) {
        FileUtils.mkdirIfNotExists(file.getParentFile());
        try (FileWriter fileWriter = new FileWriter(file, append);) {
            fileWriter.write(content);
        } catch (IOException e) {
            throw new ContextRuntimeException("写文件失败", e);
        }
    }

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
        // 声明图片后缀名数组
        String[][] imageType = {{"bmp", "0"}, {"dib", "1"},
                {"gif", "2"}, {"jfif", "3"}, {"jpe", "4"},
                {"jpeg", "5"}, {"jpg", "6"}, {"png", "7"},
                {"tif", "8"}, {"tiff", "9"}, {"ico", "10"}};
        // 遍历名称数组
        for (int i = 0; i < imageType.length; i++) {
            // 判断单个类型文件的场合
            if (imageType[i][0].equalsIgnoreCase(filename)) {
                return true;
            }
        }
        return false;
    }
    /**
     * 把数据库中图片byte，存到项目temp目录下，并且把路径返设置给TsIcon
     * @param dataGridData
     * @param request
     */
    public static void convertDataGrid(DataGridData dataGridData, HttpServletRequest request) {
        String fileDirName = request.getSession().getServletContext().getRealPath("") + File.separator + "temp";
        deleteIfExists(fileDirName);
        File fileDir = new File(fileDirName);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }
        try {
            List<Map<String, Object>> list = dataGridData.getRows();
            for (Map<String, Object> obj : list) {
                Icon icon = (Icon) obj;
                String fileName = "icon" + UUID.randomUUID() + "." + icon.getIconExtend();
                File tempFile = new File(fileDirName + File.separator + fileName);
                if (icon.getIconContent() != null) {
                    byte2image(icon.getIconContent(), tempFile);
                    icon.setIconPath("temp/" + fileName);
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    //byte数组到图片
    private static void byte2image(byte[] data, File file) {
        if (data.length < 3) return;
        try (FileImageOutputStream imageOutput = new FileImageOutputStream(file);){
            imageOutput.write(data, 0, data.length);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /***
     * 获取图片保存路径
     * @return
     */
    public static  String getDateDir(){
        Date date = new Date();
        return  new SimpleDateFormat("yyyy/MM/dd").format(date);
    }

    /***
     　　　   获取文件名
     @param fileName
      * @return
     **/
    public static String getFileName(String fileName) {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String realFilename=sdf.format(new Date())+ StrUtils.random(8);
        String extend = getExtend(fileName, "");// 获取文件扩展名
        if(StrUtils.isEmpty(extend)){
            return  realFilename;
        }
        return realFilename+"."+extend;//自定义文件名称
    }
    /***
     * 获取资源服务器的路径
     *
     * @return
     */
    public static  String getResourceLocalPath(){
        return  ConfigUtils.getConfigByName("resource.server.path");
    }
}
