package com.abocode.jfaster.core.common.model.json;

public class AjaxJsonBuilder {

    public static AjaxJson success() {
        return new AjaxJson();
    }

    public static AjaxJson success(String message) {
        return new AjaxJson(message);
    }

    public static AjaxJson failure(String message) {
        AjaxJson j = new AjaxJson(message);
        j.setSuccess(false);
        return j;
    }
}
