package com.abocode.jfaster.core.web.exception;
import com.abocode.jfaster.core.common.exception.BusinessException;
import com.abocode.jfaster.core.common.model.json.AjaxJson;
import com.abocode.jfaster.core.common.model.json.AjaxJsonBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.io.FileNotFoundException;
import java.util.Set;

/**
 * @author guanxf
 */
@ControllerAdvice
@ResponseBody
@Slf4j
public class DefaultExceptionHandler {
    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Object handleBusinessException(BusinessException exception) {
        log.info("错误码：{},错误信息：{}",  exception.getMessage(), exception);
        return build(DefaultExceptionMessage.UNKNOWN.getCode(),  exception.getMessage());
    }

    @ExceptionHandler(DataAccessException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public Object handleDbException(DataAccessException throwable) {
        log.error(throwable.getMessage(), throwable);
        return build(DefaultExceptionMessage.DATA_ACCESS.getCode(), DefaultExceptionMessage.DATA_ACCESS.getMessage());
    }

    @ExceptionHandler(TypeMismatchException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Object handleInvalidRequestException(TypeMismatchException throwable) {
        log.error(throwable.getMessage(), throwable);
        return build(DefaultExceptionMessage.PARAMETER_MISMATCH.getCode(), DefaultExceptionMessage.PARAMETER_MISMATCH.getMessage());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public Object handleInvalidRequestException(HttpMessageNotReadableException throwable) {
        log.error(throwable.getMessage(), throwable);
        return build(DefaultExceptionMessage.INVALID_REQUEST.getCode(), DefaultExceptionMessage.INVALID_REQUEST.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Object handleMethodArgumentNotValidException(MethodArgumentNotValidException throwable) {
        log.error("{}", throwable.getMessage(), throwable);
        BindingResult result = throwable.getBindingResult();
        FieldError error = result.getFieldError();
        String field = error.getField();
        String code = error.getDefaultMessage();
        String message = String.format("%s:%s", field, code);
        return build(DefaultExceptionMessage.PARAMETER_INVALID.getCode(), message);

    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public Object handleInvalidRequestException(IllegalArgumentException throwable) {
        log.error(throwable.getMessage(), throwable);
        return build(DefaultExceptionMessage.INVALID_REQUEST.getCode(), DefaultExceptionMessage.INVALID_REQUEST.getMessage());
    }

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public Object handleOtherException(Throwable throwable) {
        log.error(throwable.getMessage(), throwable);
        return build(DefaultExceptionMessage.UNKNOWN.getCode(), DefaultExceptionMessage.UNKNOWN.getMessage());
    }




    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(FileNotFoundException.class)
    public AjaxJson handleFileNotFoundException(FileNotFoundException e) {
        log.error("文件路径不存在", e);
        return build(DefaultExceptionMessage.FILE_NOT_FOUND.getCode(), DefaultExceptionMessage.FILE_NOT_FOUND.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public AjaxJson handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        log.error("缺少请求参数", e);
        return build(DefaultExceptionMessage.MISSING_PARAMETER.getCode(), DefaultExceptionMessage.MISSING_PARAMETER.getMessage());
    }

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(BindException.class)
    public AjaxJson handleBindException(BindException e) {
        log.error("参数绑定失败", e);
        BindingResult result = e.getBindingResult();
        FieldError error = result.getFieldError();
        String field = error.getField();
        String code = error.getDefaultMessage();
        String message = String.format("%s:%s", field, code);
        return build(DefaultExceptionMessage.PARAMETER_INVALID.getCode(), message);
    }
    /**
     * 400 - Bad Request
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(ConstraintViolationException.class)
    public AjaxJson handleServiceException(ConstraintViolationException e) {
        log.error("参数验证失败", e);
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        ConstraintViolation<?> violation = violations.iterator().next();
        String message = violation.getMessage();
        return build(DefaultExceptionMessage.INVALID_REQUEST.getCode(),message);
    }

    /**
     * 400 - Bad Request
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(ValidationException.class)
    public AjaxJson handleValidationException(ValidationException e) {
        log.error("参数验证失败", e);
        return build(DefaultExceptionMessage.INVALID_REQUEST.getCode(), DefaultExceptionMessage.INVALID_REQUEST.getMessage());
    }


    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public AjaxJson handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        log.error("不支持当前请求方法", e);
        return build(DefaultExceptionMessage.METHOD_NOT_SUPPORTED.getCode(), DefaultExceptionMessage.METHOD_NOT_SUPPORTED.getMessage());
    }

    /**
     * 415 - Unsupported Media Type
     */
    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public AjaxJson handleHttpMediaTypeNotSupportedException(Exception e) {
        log.error("不支持当前媒体类型", e);
        return build(DefaultExceptionMessage.MEDIA_TYPE_NOT_SUPPORTED.getCode(), DefaultExceptionMessage.MEDIA_TYPE_NOT_SUPPORTED.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public Object handleException(Exception e) {
        log.error(e.getMessage(), e);
        return build(DefaultExceptionMessage.UNKNOWN.getCode(), DefaultExceptionMessage.UNKNOWN.getMessage());
    }


    private AjaxJson build(String errorCode, String errorMessage) {
        log.error(errorCode);
        return AjaxJsonBuilder.failure(errorMessage);
    }
}
