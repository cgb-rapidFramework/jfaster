package com.abocode.jfaster.core.persistence.hibernate.hql.vo;

public enum HqlParseEnum {

    SUFFIX_COMMA(",","多条数据"),
    SUFFIX_ASTERISK("*","模糊查询条件"),
    SUFFIX_REG_ASTERISK("[*]","模糊查询条件"),
    SUFFIX_ASTERISK_VAGUE("%","模糊查询值"),
    SUFFIX_NOT_EQUAL("!","不等于"),
    SUFFIX_NOT_EQUAL_NULL("!NULL","不等于空"),
    SUFFIX_KG(" ","空格"),
    SUFFIX_NULL_STRING("","空格");

    private String value;

    private String msg;

    HqlParseEnum(String value,String msg){
        this.value = value;
        this.msg = msg;
    }

    public String getValue() {
        return value;
    }

    public String getMsg() {
        return msg;
    }

}
