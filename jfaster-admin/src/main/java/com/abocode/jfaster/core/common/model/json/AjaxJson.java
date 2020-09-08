package com.abocode.jfaster.core.common.model.json;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Map;
import java.util.Set;

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
        ValidatorFactory vf = Validation.buildDefaultValidatorFactory();
        Validator validator = vf.getValidator();
        Set<ConstraintViolation<Object>> set = validator.validate(obj);
        Assert.isTrue(CollectionUtils.isEmpty(set),"数据不合法，验证失败");
        return AjaxJsonBuilder.success("校验成功");
    }
}
