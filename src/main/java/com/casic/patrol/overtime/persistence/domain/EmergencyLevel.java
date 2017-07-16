package com.casic.patrol.overtime.persistence.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lenovo on 2017/2/10.
 */
public enum EmergencyLevel {
    BIG_LEVEL("特大事故", 1000),
    FIRST_LEVEL("一级事故", 100),
    SECOND_LEVEL("二级事故", 50),
    THIRD_LEVEL("三级事故", 25),
    FOURTH_LEVEL("四级事故", 10),
    UNKNOWN("未知", 0);

    /** 该Level的等级程度.(数字越大表示越严重) */
    private int level;

    /** 该Level的中文名称. */
    private String desc;

    private EmergencyLevel(String desc, int level) {
        this.desc = desc;
        this.level = level;
    }

    public static Map<String, EmergencyLevel> map;
    public static List<String> emergencyLevels;

    static {
        map = new HashMap<String, EmergencyLevel>();
        emergencyLevels = new ArrayList<String>();
        for (EmergencyLevel level : values()) {
            map.put(level.getDesc(), level);
            if (level != UNKNOWN) {
                emergencyLevels.add(level.getDesc());
            }
        }
    }

    public int getLevel() {
        return level;
    }

    public String getDesc() {
        return desc;
    }

    public static EmergencyLevel get(String name) {
        try {
            return valueOf(name);
        } catch (Exception e) {
            return UNKNOWN;
        }
    }

    public static EmergencyLevel getByDesc(String desc) {
        return map.get(desc) == null ? UNKNOWN : map.get(desc);
    }
}
