package com.abocode.jfaster.core.web.exception;

/**
 * 前端错误码定义.
 * @author guanxf
 */
public enum DefaultExceptionMessage implements ExceptionMessage {

    /***************************** 全局错误码 begin ***********************************/
    UNKNOWN("UNKNOWN", "SYSTEM_0000", "未知错误"),
    INVALID_REQUEST("INVALID_REQUEST", "SYSTEM_0001", "参数格式错误"),
    PARAMETER_MISMATCH("PARAMETER_MISMATCH", "SYSTEM_0002", "参数不匹配"),
    PARAMETER_INVALID("PARAMETER_INVALID", "SYSTEM_0003", "参数不合法"),
    SC_UNAUTHORIZED("SC_UNAUTHORIZED", "SYSTEM_0004", "用户未登录，没有权限访问该资源"),
    DATA_ACCESS("DATA_ACCESS", "SYSTEM_1000", "系统异常"),
    FILE_NOT_FOUND("FILE_NOT_FOUND", "SYSTEM_1001", "文件不存在"),
    MISSING_PARAMETER("MISSING_PARAMETER", "SYSTEM_1002", "值为空"),
    MEDIA_TYPE_NOT_SUPPORTED("MEDIA_TYPE_NOT_SUPPORTED", "SYSTEM_1003", "该媒体不被支持"),
    METHOD_NOT_SUPPORTED("METHOD_NOT_SUPPORTED", "SYSTEM_1004", "该方法不被支持"),
    UNSAFE_REQUEST("UNSAFE_REQUEST", "SYSTEM_1005", "非法请求"),
    VERIFY_FAIL("VERIFY_FAIL", "SYSTEM_1007", "验证失败"),
    PASSWORD_ERROR("PASSWORD_ERROR", "SYSTEM_1008", "密码输入错误"),
    OLD_PASSWORD_ERROR("OLD_PASSWORD_ERROR", "SYSTEM_1009", "旧密码输入错误"),
    TOKEN_INVALID("TOKEN_INVALID", "SYSTEM_1010", "提交失败，请刷新页面后重试"),
    /***************************** 全局错误码 end ***********************************/

    /***************************** 图片识别码错误码 begin ***********************************/
    PICTURE_INVALID("PICTURE_INVALID", "AUTH_004", "图片识别码输入不正确");
    /***************************** 图片识别码错误码 end ***********************************/

    private String key;
    private String code;
    private String message;

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getKey() {
        return key;
    }


    @Override
    public String getMessage() {
        return message;
    }


    DefaultExceptionMessage(String key, String code, String message) {
        this.key = key;
        this.code = code;
        this.message = message;
    }
    @Override
    public String toString() {
        return "DefaultExceptionMessage{" +
                "key='" + key + '\'' +
                ", code='" + code + '\'' +
                ", msg='" + message + '\'' +
                '}';
    }
}
