package com.casic.patrol.overtime.persistence.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2017/2/10.
 * 可以随意增加事故类型
 */
public enum EventType {
    BIG_LEVEL("供水管线事故"),
    FIRST_LEVEL("排水管线事故"),
    SECOND_LEVEL("电力管线事故"),
    THIRD_LEVEL("燃气管线事故"),
    FOURTH_LEVEL("电信管线事故"),
    UNKNOWN("未知");

    /** 该type的中文名称. */
    private String desc;

    private EventType(String desc) {
        this.desc = desc;
    }

    public static List<String> eventTypes;

    static {
        eventTypes = new ArrayList<String>();
        for (EventType level : values()) {
            if (level != UNKNOWN) {
                eventTypes.add(level.getDesc());
            }
        }
    }

    public String getDesc() {
        return desc;
    }

    public static EventType get(String name) {
        try {
            return valueOf(name);
        } catch (Exception e) {
            return UNKNOWN;
        }
    }
}
