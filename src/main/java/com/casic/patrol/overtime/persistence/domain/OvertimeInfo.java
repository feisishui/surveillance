package com.casic.patrol.overtime.persistence.domain;

import javax.persistence.*;

/**
 * Overtime 超时处理时间信息.
 * 
 * @author Wangpeng
 */
@Entity
@Table(name = "CD_OVERTIME_INFO")
@SequenceGenerator(name = "CD_SEQ_OvertimeINFO",sequenceName = "CD_SEQ_OvertimeINFO",allocationSize = 1,initialValue = 1)
public class OvertimeInfo implements java.io.Serializable {

    /** 主键. */
    private Long id;

    /** 起始任务名称 */
    private String startTaskName;

    /** 起始任务编码 */
    private String startTaskCode;

    /** 规则名称 */
    private String regulationName;

    /** 结束任务名称 */
    private String endTaskName;

    /** 结束任务编码 */
    private String endTaskCode;

    /** 事故类型 暂时与event-report.json中eventType对应 */
    private String eventType;

    /** 事故级别 暂时与event-confirm.json中emergencyLevel对应 */
    private String emergencyLevel;

    /** 事故对应的流程名称 */
    private String processDefinitionKey;

    /** 警告时限（单位：小时） */
    private Float warnTime;

    /** 超时时限（单位：小时） */
    private Float alarmTime;

    /** 扩展字段1. */
    private String attr1;

    /** 扩展字段2. */
    private String attr2;

    /** 扩展字段3. */
    private String attr3;

    /** 扩展字段4. */
    private String attr4;

    /** 扩展字段5. */
    private String attr5;

    @Id
    @Column(name = "DBID", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CD_SEQ_OvertimeINFO")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "START_TASK_NAME", length = 200)
    public String getStartTaskName() {
        return startTaskName;
    }

    public void setStartTaskName(String startTaskName) {
        this.startTaskName = startTaskName;
    }

    @Column(name = "START_TASK_CODE", length = 200)
    public String getStartTaskCode() {
        return startTaskCode;
    }

    public void setStartTaskCode(String startTaskCode) {
        this.startTaskCode = startTaskCode;
    }

    @Column(name = "REGULATION_NAME", length = 200)
    public String getRegulationName() {
        return regulationName;
    }

    public void setRegulationName(String regulationName) {
        this.regulationName = regulationName;
    }

    @Column(name = "END_TASK_NAME", length = 200)
    public String getEndTaskName() {
        return endTaskName;
    }

    public void setEndTaskName(String endTaskName) {
        this.endTaskName = endTaskName;
    }

    @Column(name = "END_TASK_CODE", length = 200)
    public String getEndTaskCode() {
        return endTaskCode;
    }

    public void setEndTaskCode(String endTaskCode) {
        this.endTaskCode = endTaskCode;
    }

    @Column(name = "EVENT_TYPE", length = 200)
    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    @Column(name = "EMERGENCY_LEVEL", length = 200)
    public String getEmergencyLevel() {
        return emergencyLevel;
    }

    public void setEmergencyLevel(String emergencyLevel) {
        this.emergencyLevel = emergencyLevel;
    }

    @Column(name = "PROCESS_DEFINITION_KEY", length = 200)
    public String getProcessDefinitionKey() {
        return processDefinitionKey;
    }

    public void setProcessDefinitionKey(String processDefinitionKey) {
        this.processDefinitionKey = processDefinitionKey;
    }

    @Column(name = "WARN_TIME", length = 200)
    public Float getWarnTime() {
        return warnTime;
    }

    public void setWarnTime(Float warnTime) {
        this.warnTime = warnTime;
    }

    @Column(name = "ALARM_TIME", length = 200)
    public Float getAlarmTime() {
        return alarmTime;
    }

    public void setAlarmTime(Float alarmTime) {
        this.alarmTime = alarmTime;
    }

    @Column(name = "ATTR1", length = 100)
    public String getAttr1() {
        return attr1;
    }

    public void setAttr1(String attr1) {
        this.attr1 = attr1;
    }

    @Column(name = "ATTR2", length = 100)
    public String getAttr2() {
        return attr2;
    }

    public void setAttr2(String attr2) {
        this.attr2 = attr2;
    }

    @Column(name = "ATTR3", length = 100)
    public String getAttr3() {
        return attr3;
    }

    public void setAttr3(String attr3) {
        this.attr3 = attr3;
    }

    @Column(name = "ATTR4", length = 100)
    public String getAttr4() {
        return attr4;
    }

    public void setAttr4(String attr4) {
        this.attr4 = attr4;
    }

    @Column(name = "ATTR5", length = 100)
    public String getAttr5() {
        return attr5;
    }

    public void setAttr5(String attr5) {
        this.attr5 = attr5;
    }
}
