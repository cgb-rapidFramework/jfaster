package com.abocode.jfaster.web.utils;

import com.abocode.jfaster.core.def.ConstantsDefs;
import com.abocode.jfaster.core.util.LogUtils;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import java.io.*;
import java.util.ResourceBundle;

/**
 * @author 张代浩
 */
public class JeecgSqlUtil {

    /**
     * SQL文件路径获取参数
     */
    private static final String KEY_01 = "service";
    private static final String KEY_02 = "impl";
    private static final String KEY_03 = "Impl.";
    private static final String PACKAGE_SQL = "sql";
    private static final String SUFFIX_SQL = ".sql";
    private static final String SUFFIX_D = ".";
    private static final String SUFFIX_X = "/";


    private static final ResourceBundle bundle = java.util.ResourceBundle.getBundle("sysConfig");
    private static Cache dictCache;

    static {
        if (dictCache == null) {
            dictCache = CacheManager.getInstance().getCache("dictCache");
        }
    }

    /**
     * 从文件中读取文本内容, 读取时使用平台默认编码解码文件中的字节序列
     *
     * @param file 目标文件
     * @return
     * @throws IOException
     */
    private static String loadStringFromFile(File file) throws IOException {
        return loadStringFromFile(file, "UTF-8");
    }

    /**
     * 从文件中读取文本内容
     *
     * @param file     目标文件
     * @param encoding 目标文件的文本编码格式
     * @return
     * @throws IOException
     */
    private static String loadStringFromFile(File file, String encoding)
            throws IOException {
        StringBuilder builder = new StringBuilder();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(
                    new FileInputStream(file), encoding));
            char[] chars = new char[4096];
            int length;
            while (0 < (length = reader.read(chars))) {
                builder.append(chars, 0, length);
            }
        } finally {
            try {
                if (reader != null)
                    reader.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return builder.toString();


    }

    /**
     * 读取SQL内容
     *
     * @param fileUrl
     * @throws IOException
     */

    private static String getFlieTxt(String fileUrl) {
        String sql = null;
        try {
            sql = loadStringFromFile(new File(fileUrl));
        } catch (IOException e) {
            LogUtils.error(e.getMessage());
        }
        return sql;
    }


    public static String getMethodSql(String methodUrl) {

        //[1].开发模式：dev SQL文件每次都加载
        if (ConstantsDefs.MODE_DEVELOP.equals(bundle.getObject("sqlReadMode"))) {
            return getMethodSqlLogicJar(methodUrl);
        }
        //[2].发布模式：pub SQL文件只加载一次
        else if (ConstantsDefs.MODE_PUBLISH.equals(bundle.getObject("sqlReadMode"))) {
            Element element = dictCache.get(methodUrl);
            if (element == null) {
                element = new Element(methodUrl, (Serializable) getMethodSqlLogicJar(methodUrl));
                //永久缓存
                dictCache.put(element);
            }
            return element.getValue().toString();
        } else {
            return "";
        }
    }

    /**
     * 新获取SQL路径方法,并读取文件获取SQL内容
     *
     * @param methodUrl
     * @return
     */
    public static String getMethodSqlLogic(String methodUrl) {
        String head = methodUrl.substring(0, methodUrl.indexOf(KEY_01));
        String end = methodUrl.substring(methodUrl.indexOf(KEY_02) + KEY_02.length()).replace(KEY_03, "_");
        String sqlurl = head + PACKAGE_SQL + end;
        sqlurl = sqlurl.replace(SUFFIX_D, SUFFIX_X);
        sqlurl = sqlurl + SUFFIX_SQL;

        String projectPath = ClassLoaderUtils.getAppPath(JeecgSqlUtil.class);
        sqlurl = projectPath + SUFFIX_X + sqlurl;
        LogUtils.info(sqlurl);
        return getFlieTxt(sqlurl);
    }

    /**
     * 新获取SQL路径方法,并读取文件获取SQL内容
     * 扩展可以读取jar中sql
     *
     * @param methodUrl
     * @return
     */
    public static String getMethodSqlLogicJar(String methodUrl) {
        StringBuffer sb = new StringBuffer();
        String head = methodUrl.substring(0, methodUrl.indexOf(KEY_01));
        String end = methodUrl.substring(methodUrl.indexOf(KEY_02) + KEY_02.length()).replace(KEY_03, "_");
        String sqlurl = head + PACKAGE_SQL + end;
        sqlurl = sqlurl.replace(SUFFIX_D, SUFFIX_X);
        sqlurl = SUFFIX_X + sqlurl + SUFFIX_SQL;

//		返回读取指定资源的输入流   
        InputStream is = JeecgSqlUtil.class.getResourceAsStream(sqlurl);
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String s = "";
        try {
            while ((s = br.readLine()) != null)
                sb.append(s + " ");
        } catch (IOException e) {
            LogUtils.error(e.getMessage());
        }
        return sb.toString();
    }

    /**
     * 根据当前SQL，拼装出查询总数的SQL
     *
     * @param sql 当前SQL语句
     * @return
     */
    public static String getCountSqlBySql(String sql) {
        String countSql = "SELECT COUNT(*)  ";

        String upperSql = sql.toUpperCase();
        int fromIndex = upperSql.indexOf("FROM");
        int whereIndex = upperSql.indexOf("WHERE");

        if (whereIndex > -1) {
            countSql = countSql + sql.substring(fromIndex, whereIndex);
        } else {
            countSql = countSql + sql.substring(fromIndex);
        }
        return countSql;

    }


    /**
     * 旧获取SQL路径方法
     *
     * @param methodUrl
     * @return
     */
    @Deprecated
    public static String getMethodSqlLogicOld(String methodUrl) {
        //从Spring中获取Request
//		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
//		String projectPath = ServletActionContext.getServletContext().getRealPath("/");
//		org.jeecgframework.core.util.LogUtil.info(projectPath);
//		String projectPath =  request.getSession().getServletContext().getRealPath("/");
        methodUrl = methodUrl.substring(17).replace("Impl", "").replace(".", "/");
        String[] str = methodUrl.split("/");
        StringBuffer sb = new StringBuffer();
        int num = 2;
        int length = str.length;
        for (String s : str) {

            if (num < length) {
                sb.append(s);
                sb.append("/");
            } else if (num == length) {
                sb.append(s);
                sb.append("_");
            } else {
                sb.append(s);
            }
            num = num + 1;
        }
        String projectPath = ClassLoaderUtils.getAppPath(JeecgSqlUtil.class);
        String fileUrl = projectPath + "/sun/sql/" + sb.toString() + ".sql";
        return getFlieTxt(fileUrl);
    }


    public static void main(String[] args) {
        //LogUtils.info(getAppPath(JeecgSqlUtil.class));
        LogUtils.info(getCountSqlBySql("SELECT * 	from JEECG_DICT_PARAM WHERE 1=1"));
    }
}
