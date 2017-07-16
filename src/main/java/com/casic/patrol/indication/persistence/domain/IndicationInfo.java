package com.casic.patrol.indication.persistence.domain;

import javax.persistence.*;
import java.util.Date;

/**
 * TaskInfo 指标信息.
 * 
 * @author Wangpeng
 */
@Entity
@Table(name = "INDICATION_INFO")
@SequenceGenerator(name = "CD_SEQ_IndicationINFO",sequenceName = "CD_SEQ_IndicationINFO",allocationSize = 1,initialValue = 1)
public class IndicationInfo implements java.io.Serializable {

    /** 主键. */
    private Long id;

    /** 关联的任务ID. */
    private String taskId;

    /** 关联的流程ID. */
    private String instanceId;

    /** 代码. */
    private String code;

    /** 关联的任务名称. */
    private String taskName;

    /** 指标描述. */
    private String description;

    /** 指标分类. */
    private String catalog;

    /** 指标关联人. */
    private String ref;

    /** 指标观察任务ID. */
    private String relatedTaskId;

    /** 指标观察任务起始时间. */
    private Date relatedTaskStartTime;

    /** 指标观察任务结束事件. */
    private Date relatedTaskEndTime;

    /** 创建时间. */
    private Date createTime;

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
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CD_SEQ_IndicationINFO")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "TASK_ID", length = 200)
    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    @Column(name = "INSTANCE_ID", length = 200)
    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    @Column(name = "CODE", length = 100)
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Column(name = "TASK_NAME", length = 200)
    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    @Column(name = "DESCRIPTION", length = 200)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "CATEGORY", length = 100)
    public String getCatalog() {
        return catalog;
    }

    public void setCatalog(String catalog) {
        this.catalog = catalog;
    }

    @Column(name = "REF", length = 200)
    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    @Column(name = "RELATED_TASK_ID", length = 200)
    public String getRelatedTaskId() {
        return relatedTaskId;
    }

    public void setRelatedTaskId(String relatedTaskId) {
        this.relatedTaskId = relatedTaskId;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "RELATED_TASK_START_TIME", length = 26)
    public Date getRelatedTaskStartTime() {
        return relatedTaskStartTime;
    }

    public void setRelatedTaskStartTime(Date relatedTaskStartTime) {
        this.relatedTaskStartTime = relatedTaskStartTime;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "RELATED_TASK_END_TIME", length = 26)
    public Date getRelatedTaskEndTime() {
        return relatedTaskEndTime;
    }

    public void setRelatedTaskEndTime(Date relatedTaskEndTime) {
        this.relatedTaskEndTime = relatedTaskEndTime;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATE_TIME", length = 26)
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
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
