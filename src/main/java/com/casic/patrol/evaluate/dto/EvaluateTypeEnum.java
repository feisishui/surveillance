package com.casic.patrol.evaluate.dto;

/**
 * Created by admin on 2017/4/6.
 */
public enum EvaluateTypeEnum {


    any("任意周期考核",0),
    YEAR("年度考核",1),
    QUARTER("季度考核",2),
    MONTH("月度考核",3),
    OTHER("未知",-1);

    // 成员变量
    private String name;
    private int index;
    // 构造方法
    private EvaluateTypeEnum(String name, int index){
        this.name=name;
        this.index=index;
    }

    public String getName() {
        return name;
    }

    public int getIndex() {
        return index;
    }

    //覆盖方法
    @Override
    public String toString(){
        return this.name;
    }

    public static EvaluateTypeEnum getByIndex(String index) {
        try {
            for (EvaluateTypeEnum temp : values()) {
                if (temp.index == Integer.parseInt(index)) {
                    return temp;
                }
            }
        } catch (Exception e) {}
        return OTHER;
    }

    public static EvaluateTypeEnum getByName(String name) {
        try {
            for (EvaluateTypeEnum temp : values()) {
                if (name.equals(temp.name)) {
                    return temp;
                }
            }
        } catch (Exception e) {}
        return OTHER;
    }
}
