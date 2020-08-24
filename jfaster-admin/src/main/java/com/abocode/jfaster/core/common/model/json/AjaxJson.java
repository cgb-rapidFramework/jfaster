package com.abocode.jfaster.core.common.model.json;

import java.util.Map;
import java.util.Set;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

/**
 * $.ajax后需要接受的JSON
 *
 * @author
 */
@NoArgsConstructor
@Data
public class AjaxJson {
    private boolean success = true;// 是否成功
    private String msg = "操作成功";// 提示信息
    private Object obj = null;// 其他信息
    private Map<String, Object> attributes;// 其他参数

    public AjaxJson(String msg) {
        this.msg = msg;
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public AjaxJson setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
        return this;
    }

    public AjaxJson volatileBean(Object obj) {
        AjaxJson j = new AjaxJson();
        j.setMsg("校验成功");
        ValidatorFactory vf = Validation.buildDefaultValidatorFactory();
        Validator validator = vf.getValidator();
        Set<ConstraintViolation<Object>> set = validator.validate(obj);
        for (ConstraintViolation<Object> constraintViolation : set) {
            j.setMsg("数据不合法:值" + constraintViolation.getInvalidValue() + " " + constraintViolation.getMessage());
            j.setSuccess(false);
            return j;
        }
        return j;
    }
}
