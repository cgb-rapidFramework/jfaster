package com.abocode.jfaster.core.common.constants;

public final class Globals {
    /**
     * 保存用户到SESSION
     */
    public static String USER_SESSION = "USER_SESSION";
    /**
     * 正常
     */
    public static Short User_Normal = 1;
    /***
     * 禁用
     */
    public static Short User_Forbidden = 0;
    /***
     * 超级管理员
     */
    public static Short User_ADMIN = -1;
    /**
     * 日志级别定义
     */
    public static Short Log_Leavel_INFO = 1;
    /**
     * 日志类型
     */
    public static Short Log_Type_LOGIN = 1; //登陆
    public static Short Log_Type_EXIT = 2;  //退出
    public static Short Log_Type_INSERT = 3; //插入
    public static Short Log_Type_DEL = 4; //删除
    public static Short Log_Type_UPDATE = 5; //更新
    /**
     * 权限等级
     */
    public static Short Function_Leave_ONE = 0;//一级权限
    /**
     * 没有勾选的操作code
     */
    public static String NOAUTO_OPERATIONCODES = "noauto_operationCodes";
    /**
     * 勾选了的操作code
     */
    public static String OPERATIONCODES = "operationCodes";
    /**
     * 权限类型
     */
    public static Short OPERATION_TYPE_HIDE = 0;//页面
    /**
     * 数据权限 - 菜单数据规则集合
     */
    public static String MENU_DATA_AUTHOR_RULES = "MENU_DATA_AUTHOR_RULES";
    /**
     * 数据权限 - 菜单数据规则sql
     */
    public static String MENU_DATA_AUTHOR_RULE_SQL = "MENU_DATA_AUTHOR_RULE_SQL";
    /**
     * 配置系统是否开启按钮权限控制
     */
    public static boolean AUTHORITY_BUTTON_CHECK = false;
    public static boolean AUTHORITY_IS_OPEN = false;

}
