package com.abocode.jfaster.api.system;

import lombok.Data;

@Data
public class ConfigDto {
    private String id;
    private String code;
    private String name;
    private String content;
    private String note;
}
