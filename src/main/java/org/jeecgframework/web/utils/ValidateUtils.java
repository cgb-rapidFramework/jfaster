package org.jeecgframework.web.utils;

import org.jeecgframework.core.common.model.json.AjaxJson;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

/**
 * Created by guanxf on 2016/2/23.
 */
public class ValidateUtils {
    /**
     * 检验bean是否数据合法
     * @param obj
     * @return
     */
    public static AjaxJson volatileBean(Object obj){
        AjaxJson j=new AjaxJson();
        j.setMsg("校验成功");
        ValidatorFactory vf = Validation.buildDefaultValidatorFactory();
        Validator validator = vf.getValidator();
        Set<ConstraintViolation<Object>> set = validator.validate(obj);
        for (ConstraintViolation<Object> constraintViolation : set) {
            j.setMsg("数据不合法:值"+constraintViolation.getInvalidValue()+" "+constraintViolation.getMessage());
            j.setSuccess(false);
           return  j;
        }
        return  j;
    }
}
