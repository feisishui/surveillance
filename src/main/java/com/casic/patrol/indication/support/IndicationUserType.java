package com.casic.patrol.indication.support;

/**
 * Created by lenovo on 2017/5/24.
 */
public enum IndicationUserType {
    PATROL("巡查部门", ""),
    CENTER("指挥中心", ""),
    COMPANY("专业公司", ""),
    UNKNOWN("未知", "");

    /** 该指标的中文名称. */
    private String name;

    /** 该指标的英文名称. */
    private String desc;

    private IndicationUserType(String name, String desc) {
        this.name = name;
        this.desc = desc;
    }

    public static IndicationUserType getByName(String name) {
        try {
            for (IndicationUserType type : values()) {
                if (type.name.equals(name)) {
                    return type;
                }
            }
            return UNKNOWN;
        } catch (Exception e) {
            return UNKNOWN;
        }
    }
}
