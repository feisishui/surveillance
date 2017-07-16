package com.casic.patrol.overtime.persistence.domain;

import javax.persistence.*;
import java.util.Date;

/**
 * TaskOvertime 任务实例超时处理时间信息.
 * 
 * @author Wangpeng
 */
@Entity
@Table(name = "CD_TASK_OVERTIME_INFO")
@SequenceGenerator(name = "CD_SEQ_TaskOvertimeINFO",sequenceName = "CD_SEQ_TaskOvertimeINFO",allocationSize = 1,initialValue = 1)
public class TaskOvertimeInfo implements java.io.Serializable {

    /** 主键. */
    private Long id;

    /** 关联的流程实例ID. */
    private String processInstanceId;

    /** 关联的流程定义ID. */
    private String processDefinitionId;

    /** 业务标识. */
    private String businessKey;

    /** 事故类型 暂时与event-report.json中eventType对应 */
    private String eventType;

    /** 事故级别 暂时与event-confirm.json中emergencyLevel对应 */
    private String emergencyLevel;

    /** 该统计起始任务的task code. */
    private String startTaskCode;

    /** 该统计起始任务关联的任务实例id. */
    private String startTaskId;

    /** 该统计结束任务的task code. */
    private String endTaskCode;

    /** 该统计结束任务关联的任务实例id. */
    private String endTaskId;

    /** 开始时间. */
    private Date startTime;

    /** 警告时间. */
    private Date warnTime;

    /** 超时时间. */
    private Date overTime;

    /** 结束时间. */
    private Date endTime;

    /** 是否计时已经完成 */
    private boolean finished;

    /** 关联超时规则 */
    private OvertimeInfo overtimeInfo;

    /** 超时状态 */
    private String levelCode;

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
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CD_SEQ_TaskOvertimeINFO")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "PROCESS_INSTANCE_ID", length = 200)
    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    @Column(name = "PROCESS_DEFINITION_ID", length = 200)
    public String getProcessDefinitionId() {
        return processDefinitionId;
    }

    public void setProcessDefinitionId(String processDefinitionId) {
        this.processDefinitionId = processDefinitionId;
    }

    @Column(name = "BUSINESS_KEY", length = 100)
    public String getBusinessKey() {
        return businessKey;
    }

    public void setBusinessKey(String businessKey) {
        this.businessKey = businessKey;
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

    @Column(name = "START_TASK_CODE", length = 100)
    public String getStartTaskCode() {
        return startTaskCode;
    }

    public void setStartTaskCode(String startTaskCode) {
        this.startTaskCode = startTaskCode;
    }

    @Column(name = "START_TASK_ID", length = 200)
    public String getStartTaskId() {
        return startTaskId;
    }

    public void setStartTaskId(String startTaskId) {
        this.startTaskId = startTaskId;
    }

    @Column(name = "END_TASK_CODE", length = 100)
    public String getEndTaskCode() {
        return endTaskCode;
    }

    public void setEndTaskCode(String endTaskCode) {
        this.endTaskCode = endTaskCode;
    }

    @Column(name = "END_TASK_ID", length = 200)
    public String getEndTaskId() {
        return endTaskId;
    }

    public void setEndTaskId(String endTaskId) {
        this.endTaskId = endTaskId;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "START_TIME", length = 26)
    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "WARN_TIME", length = 26)
    public Date getWarnTime() {
        return warnTime;
    }

    public void setWarnTime(Date warnTime) {
        this.warnTime = warnTime;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "OVER_TIME", length = 26)
    public Date getOverTime() {
        return overTime;
    }

    public void setOverTime(Date overTime) {
        this.overTime = overTime;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "END_TIME", length = 26)
    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    @Column(name = "FINISHED")
    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    @Column(name = "LEVEL_CODE", length = 100)
    public String getLevelCode() {
        return levelCode;
    }

    public void setLevelCode(String levelCode) {
        this.levelCode = levelCode;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "OVERTIME_ID")
    public OvertimeInfo getOvertimeInfo() {
        return overtimeInfo;
    }

    public void setOvertimeInfo(OvertimeInfo overtimeInfo) {
        this.overtimeInfo = overtimeInfo;
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
