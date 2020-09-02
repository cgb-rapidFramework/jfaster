package com.abocode.jfaster.core.common.exception;
/**
 * Description:
 *
 * @author: guanxf
 * @date: 2019/12/11
 */
public class ContextRuntimeException extends RuntimeException {
    public ContextRuntimeException(Exception exception) {
        super(exception);
    }
    public ContextRuntimeException(String message) {
         super(message);
    }
    public ContextRuntimeException(String message, Exception exception) {
        super(message, exception);
    }
}
