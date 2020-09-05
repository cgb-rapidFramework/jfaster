package com.abocode.jfaster.core.common.constants;

public final class Globals {
    private Globals() {
    }

    /**
     * 保存用户到SESSION
     */
    public static  final String USER_SESSION = "USER_SESSION";
    /**
     * 正常
     */
    public static  final Short USER_NORMAL = 1;
    /***
     * 禁用
     */
    public static  final Short USER_FORBIDDEN = 0;
    /***
     * 超级管理员
     */
    public static  final Short USER_ADMIN = -1;
    /**
     * 日志级别定义
     */
    public static  final Short LOG_LEVEL = 1;
    /**
     * 日志类型
     */
    public static  final Short LOG_TYPE_LOGIN = 1; //登陆
    public static  final Short LOG_TYPE_EXIT = 2;  //退出
    public static  final Short LOG_TYPE_INSERT = 3; //插入
    public static  final Short LOG_TYPE_DEL = 4; //删除
    public static  final Short LOG_TYPE_UPDATE = 5; //更新
    /**
     * 没有勾选的操作code
     */
    public static  final String NO_AUTO_OPERATION_CODES = "noauto_operationCodes";
    /**
     * 勾选了的操作code
     */
    public static  final String OPERATION_CODES = "operationCodes";
    /**
     * 权限类型
     */
    public static  final Short OPERATION_TYPE_HIDE = 0;//页面
    /**
     * 数据权限 - 菜单数据规则集合
     */
    public static  final String MENU_DATA_AUTHOR_RULES = "MENU_DATA_AUTHOR_RULES";
    /**
     * 数据权限 - 菜单数据规则sql
     */
    public static  final String MENU_DATA_AUTHOR_RULE_SQL = "MENU_DATA_AUTHOR_RULE_SQL";
    /**
     * 配置系统是否开启按钮权限控制
     */
    public static  final boolean AUTHORITY_BUTTON_CHECK = false;
    public static  final boolean AUTHORITY_IS_OPEN = false;

}
