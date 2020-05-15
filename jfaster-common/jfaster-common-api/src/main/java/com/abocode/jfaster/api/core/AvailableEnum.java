package com.abocode.jfaster.api.core;

/**
 * Description:
 *
 * @author: guanxianfei
 * @date: 2019/12/5
 */
public enum AvailableEnum {

    AVAILABLE(1), UNAVAILABLE(0);

    private int value;

    AvailableEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
    public short getShortValue() {
        return (short) value;
    }
}
