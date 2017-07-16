package com.casic.patrol.overtime.persistence.domain;

/**
 * Created by wp on 2017/1/19.
 */
public enum OvertimeLevel {
    NORMAL("普通", 100, ""),
    WARN("提醒", 200, "#ffff00"),
    ALARM("报警", 300, "red"),
    UNKNOWN("未知", 10, "#808080");

    /** 该Level的等级程度.(数字越大表示越严重) */
    private int level;

    /** 该Level的中文名称. */
    private String desc;

    /** 前端展示颜色 */
    private String color;

    private OvertimeLevel(String desc, int level, String color) {
        this.desc = desc;
        this.level = level;
        this.color = color;
    }

    public int getLevel() {
        return level;
    }

    public String getDesc() {
        return desc;
    }

    public String getColor() {
        return color;
    }

    public static OvertimeLevel get(String name) {
        try {
            return valueOf(name);
        } catch (Exception e) {
            return UNKNOWN;
        }
    }

    public boolean biggerThan(OvertimeLevel l) {
        return level > l.level;
    }

    public boolean equals(String value) {
        return name().equals(value);
    }
}
