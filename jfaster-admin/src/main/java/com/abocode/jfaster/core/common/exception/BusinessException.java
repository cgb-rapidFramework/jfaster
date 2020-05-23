package com.abocode.jfaster.core.common.exception;

public class BusinessException  extends RuntimeException{
	private static final long serialVersionUID = 1L;

	public BusinessException(String message){
		super(message);
	}
	
	public BusinessException(Throwable cause)
	{
		super(cause);
	}
	
	public BusinessException(String message,Throwable cause)
	{
		super(message,cause);
	}

	private String code;
	private String message;
	private Object data;

	public BusinessException( String code,String message,Object data) {
		this.code = code;
		this.message = message;
		this.data=data;
	}

	public BusinessException( String code,String message) {
		this.code = code;
		this.message = message;
	}

	@Override
	public String getMessage() {
		return message;
	}

	public String getCode() {
		return code;
	}

	public Object getData() {
		return data;
	}
}
