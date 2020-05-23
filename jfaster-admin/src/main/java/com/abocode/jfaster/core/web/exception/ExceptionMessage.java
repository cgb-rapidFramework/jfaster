package com.abocode.jfaster.core.web.exception;

/**
 * 错误码枚举接口
 * @author guanxf
 */
public interface ExceptionMessage {
    String getCode();
    String getKey();
    String getMessage();
}
