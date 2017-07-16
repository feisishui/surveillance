package com.casic.patrol.overtime.listener;

import com.casic.patrol.bpm.support.DefaultTaskListener;
import com.casic.patrol.overtime.persistence.domain.OvertimeLevel;
import com.casic.patrol.overtime.persistence.domain.OvertimeInfo;
import com.casic.patrol.overtime.persistence.domain.TaskOvertimeInfo;
import com.casic.patrol.overtime.persistence.manager.OvertimeInfoManager;
import com.casic.patrol.overtime.persistence.manager.TaskOvertimeInfoManager;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Created by wp on 2017/1/19.
 */
public class OvertimeCalculateListener extends DefaultTaskListener {

    @Value("${overtime.emergencyLevel.name}")
    private String emergencyLevelName;

    @Value("${overtime.eventType.name}")
    private String eventTypeName;

    @Resource
    private OvertimeInfoManager overtimeInfoManager;

    @Resource
    private TaskOvertimeInfoManager taskOvertimeInfoManager;

    @Override
    public void onCreate(DelegateTask delegateTask) throws Exception {
        String startCode = delegateTask.getTaskDefinitionKey();
        String processDefinedId = ((TaskEntity) delegateTask).getProcessInstance().getProcessDefinition().getKey();
        String emergencyLevel = getVariable(delegateTask, emergencyLevelName);
        String eventType = getVariable(delegateTask, eventTypeName);
        List<OvertimeInfo> list = overtimeInfoManager.getInfoBy(
                startCode, processDefinedId, emergencyLevel, eventType
        );
        for (OvertimeInfo info : list) {
            if (info.getAlarmTime() > 0) {
                TaskOvertimeInfo taskInfo = new TaskOvertimeInfo();
                taskInfo.setProcessInstanceId(delegateTask.getProcessInstanceId());
                taskInfo.setProcessDefinitionId(processDefinedId);
                taskInfo.setBusinessKey(delegateTask.getExecution().getProcessBusinessKey());
                taskInfo.setEventType(info.getEventType());
                taskInfo.setEmergencyLevel(info.getEmergencyLevel());
                taskInfo.setStartTaskCode(info.getStartTaskCode());
                taskInfo.setStartTaskId(delegateTask.getId());
                taskInfo.setEndTaskCode(info.getEndTaskCode());
                taskInfo.setStartTime(new Date());
                taskInfo.setOverTime(new Date(
                        taskInfo.getStartTime().getTime() +
                                (long)(info.getAlarmTime() * 60 * 60 * 1000)
                ));
                taskInfo.setFinished(false);
                taskInfo.setLevelCode(OvertimeLevel.NORMAL.name());
                taskInfo.setOvertimeInfo(info);
                taskOvertimeInfoManager.save(taskInfo);
            }
        }
    }

    private String getVariable(DelegateTask delegateTask, String key) {
        String value = (String)delegateTask.getExecution().getVariable(key);
        if (value == null) {
//            return defaultVariableName;
        }
        return value;
    }

    @Override
    public void onComplete(DelegateTask delegateTask) throws Exception {
        String processDefinedId = ((TaskEntity) delegateTask).getProcessInstance().getProcessDefinition().getKey();
        String processInstanceId = delegateTask.getProcessInstanceId();
        String businessKey = delegateTask.getExecution().getProcessBusinessKey();
        String endTaskCode = delegateTask.getTaskDefinitionKey();
        List<TaskOvertimeInfo> list = taskOvertimeInfoManager.getUnfinishedInfoBy(
                processDefinedId, processInstanceId, businessKey, endTaskCode
        );
        for (TaskOvertimeInfo info : list) {
            info.setFinished(true);
            info.setEndTaskId(delegateTask.getId());
            info.setEndTime(new Date());
            taskOvertimeInfoManager.save(info);
        }
    }
}
