package com.casic.patrol.overtime.dto;

import com.casic.patrol.core.mapper.BeanMapper;
import com.casic.patrol.overtime.persistence.domain.OvertimeInfo;
import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by lenovo on 2017/1/18.
 */
public class OvertimeInfoDto {

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

    private String btnEdit = "<a href='#' class='btn mini blue'><i class='icon-edit'></i>编辑</a>";
    private String btnDelete = "<a href='#' class='btn mini red'><i class='icon-trash'></i>删除</a>";

    private static BeanMapper beanMapper = new BeanMapper();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStartTaskName() {
        return startTaskName;
    }

    public void setStartTaskName(String startTaskName) {
        this.startTaskName = startTaskName;
    }

    public String getStartTaskCode() {
        return startTaskCode;
    }

    public void setStartTaskCode(String startTaskCode) {
        this.startTaskCode = startTaskCode;
    }

    public String getRegulationName() {
        return regulationName;
    }

    public void setRegulationName(String regulationName) {
        this.regulationName = regulationName;
    }

    public String getEndTaskName() {
        return endTaskName;
    }

    public void setEndTaskName(String endTaskName) {
        this.endTaskName = endTaskName;
    }

    public String getEndTaskCode() {
        return endTaskCode;
    }

    public void setEndTaskCode(String endTaskCode) {
        this.endTaskCode = endTaskCode;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getEmergencyLevel() {
        return emergencyLevel;
    }

    public void setEmergencyLevel(String emergencyLevel) {
        this.emergencyLevel = emergencyLevel;
    }

    public String getProcessDefinitionKey() {
        return processDefinitionKey;
    }

    public void setProcessDefinitionKey(String processDefinitionKey) {
        this.processDefinitionKey = processDefinitionKey;
    }

    public Float getWarnTime() {
        return warnTime;
    }

    public void setWarnTime(Float warnTime) {
        this.warnTime = warnTime;
    }

    public Float getAlarmTime() {
        return alarmTime;
    }

    public void setAlarmTime(Float alarmTime) {
        this.alarmTime = alarmTime;
    }

    public String getAttr1() {
        return attr1;
    }

    public void setAttr1(String attr1) {
        this.attr1 = attr1;
    }

    public String getAttr2() {
        return attr2;
    }

    public void setAttr2(String attr2) {
        this.attr2 = attr2;
    }

    public String getAttr3() {
        return attr3;
    }

    public void setAttr3(String attr3) {
        this.attr3 = attr3;
    }

    public String getAttr4() {
        return attr4;
    }

    public void setAttr4(String attr4) {
        this.attr4 = attr4;
    }

    public String getAttr5() {
        return attr5;
    }

    public void setAttr5(String attr5) {
        this.attr5 = attr5;
    }

    public static OvertimeInfoDto Convert(OvertimeInfo info) {
        if (info == null) {
            return null;
        }
        OvertimeInfoDto overtimeInfoDto = new OvertimeInfoDto();
        beanMapper.copy(info, overtimeInfoDto,
                OvertimeInfo.class, OvertimeInfoDto.class);
        return overtimeInfoDto;
    }

    public static List<OvertimeInfoDto> Converts(List<OvertimeInfo> result) {
        if (CollectionUtils.isEmpty(result)) {
            return Collections.emptyList();
        }
        List<OvertimeInfoDto> infoDtos = new ArrayList<OvertimeInfoDto>();
        for (OvertimeInfo info : result) {
            OvertimeInfoDto temp = OvertimeInfoDto.Convert(info);
            if (temp != null) {
                infoDtos.add(temp);
            }
        }
        return infoDtos;
    }
}
