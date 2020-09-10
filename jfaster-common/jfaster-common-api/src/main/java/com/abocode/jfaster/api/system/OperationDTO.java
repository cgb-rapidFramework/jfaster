package com.abocode.jfaster.api.system;

import lombok.Data;

/**
 * Description:
 *
 * @author: guanxf
 * @date: 2020/9/10
 */
@Data
public class OperationDTO {
    private String id;
    private String operationName;
    private String operationCode;
    private String operationIcon;
    private Short status;
    private Short operationType;
}
