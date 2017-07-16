package com.casic.patrol.indication.support;

/**
 * Created by lenovo on 2016/9/27.
 */
public enum Indication {
    REPORT("公众上报数", "event-report", "游客事故上报数目"),
    ASSIGN("指派数", "event-assign", "指挥中心指派公众上报事故数目"),
    DISPATCH("派遣数", "event-dispatch", "指挥中心已经派遣的事故数目"),
    DISPATCH_SHOULD("应派遣数", "event-dispatch-should", "指挥中心应该派遣的事故数目"),
    DISPATCH_ONTIME("按期派遣数", "event-dispatch-on-time", "指挥中心按期派遣的事故数目"),
    DISPATCH_OVERDUE("超期派遣数", "event-dispatch-overdue", "指挥中心超期派遣事故数目"),
    UNDISPATCH_OVERDUE("超期未派遣数", "event-undispatch-overdue", "指挥中心超期未派遣的事故数目", false),

    VERIFY("核实数", "event-verify", "巡查部门已经核实的事故数目"),
    VERIFY_SHOULD("应核实数", "event-verify-should", "巡查部门应该核实的事故数目"),
    VERIFY_ONTIME("按期核实数", "event-verify-on-time", "巡查部门按期核实的事故数目"),
    VERIFY_OVERDUE("超期核实数", "event-verify-overdue", "巡查部门超期核实的事故数目"),
    UNVERIFY_OVERDUE("超期未核实数", "event-unverify-overdue", "巡查部门超期未核实的事故数目", false),
    MISSING_REPORT("漏报数", "event-missing-report", "巡查部门未上报的有效公众上报事故数目"),

    DISPOSE("处置数", "event-dispose", "专业处置部门已经处置的事故数目"),
    DISPOSE_SHOULD("应处置数", "event-dispose-should", "专业处置部门应该处置的事故数目"),
    DISPOSE_ONTIME("按期处置数", "event-dispose-on-time", "专业处置部门按期处置的事故数目"),
    DISPOSE_OVERDUE("超期处置数", "event-dispose-overdue", "专业处置部门超期处置的事故数目"),
    UNDISPOSE_OVERDUE("超期处置数", "event-undispose-overdue", "专业处置部门超期未处置的事故数目", false),
    REWORK("返工数", "event-rework", "专业处置部门未通过结案的次数"),

    UNKNOWN("未知", "unknown", "未知描述", false);

    /** 该指标的中文名称. */
    private String name;

    /** 该指标的英文名称. */
    private String code;

    /** 对该指标的描述. */
    private String desc;

    /** 该指标是否可以进行统计. */
    private Boolean couldStatistics;

    private Indication(String name, String code, String desc) {
        this.name = name;
        this.code = code;
        this.desc = desc;
        this.couldStatistics = true;
    }

    private Indication(String name, String code, String desc, Boolean couldStatistics) {
        this.name = name;
        this.code = code;
        this.desc = desc;
        this.couldStatistics = couldStatistics;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public Boolean getCouldStatistics() {
        return couldStatistics;
    }

    public String getDesc() {
        return desc;
    }

    public static Indication get(String name) {
        try {
            return valueOf(name);
        } catch (Exception e) {
            return UNKNOWN;
        }
    }
}
