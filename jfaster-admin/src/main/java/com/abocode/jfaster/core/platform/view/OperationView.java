package com.abocode.jfaster.core.platform.view;
import lombok.Data;
/**
 * 权限操作
 *  @author  guanxf
 */
@Data
public class OperationView implements java.io.Serializable {
	private String id;
	private String operationName;
	private String operationCode;
	private String operationIcon;
	private Short status;
	private Short operationType;
	private IconView icon = new IconView();
	private FunctionView function = new FunctionView();
}