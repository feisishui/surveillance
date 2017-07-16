package com.casic.patrol.indication.listener;

import com.casic.patrol.api.humantask.HumanTaskConnector;
import com.casic.patrol.api.humantask.HumanTaskDTO;
import com.casic.patrol.core.util.StringUtils;
import com.casic.patrol.indication.persistence.domain.IndicationInfo;
import com.casic.patrol.indication.support.Indication;
import org.activiti.engine.delegate.DelegateTask;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Created by lenovo on 2016/9/27.
 */
public abstract class BaseIndicationCalculateStrategy implements IndicationCalculateStrategy{
    private String processDefinitionKey;
    private List<String> taskDefinitionKey;
    public BaseIndicationCalculateStrategy(String processDefinitionKey, List<String> taskDefinitionKey) {
        this.processDefinitionKey = processDefinitionKey;
        this.taskDefinitionKey = taskDefinitionKey;
    }

    @Resource
    private HumanTaskConnector humanTaskConnector;

    @Override
    public boolean needCalculate(DelegateTask delegateTask) {
        if (StringUtils.isNotBlank(processDefinitionKey)) {
            if (!processDefinitionKey.equals(delegateTask.getProcessDefinitionId())) {
                return false;
            }
            if (taskDefinitionKey != null && taskDefinitionKey.size() > 0) {
                String tempKey = delegateTask.getTaskDefinitionKey();
                for (String key : taskDefinitionKey) {
                    if (key.equals(tempKey)) return true;
                }
                return false;
            }
        }
        return true;
    }

    public void setProcessDefinitionKey(String processDefinitionKey) {
        this.processDefinitionKey = processDefinitionKey;
    }

    public String getProcessDefinitionKey() {
        return processDefinitionKey;
    }

    public List<String> getTaskDefinitionKey() {
        return taskDefinitionKey;
    }

    public void setTaskDefinitionKey(List<String> taskDefinitionKey) {
        this.taskDefinitionKey = taskDefinitionKey;
    }

    protected IndicationInfo calculateIndication(DelegateTask delegateTask, Indication indication) {
        HumanTaskDTO humanTaskDto = humanTaskConnector
                .findHumanTaskByTaskId(delegateTask.getId());
        IndicationInfo indicationInfo = new IndicationInfo();
        indicationInfo.setCatalog(indication.name());
        indicationInfo.setCode(delegateTask.getTaskDefinitionKey());
        indicationInfo.setCreateTime(new Date());
        indicationInfo.setTaskId(delegateTask.getId());
        indicationInfo.setTaskName(delegateTask.getName());
        indicationInfo.setDescription(delegateTask.getDescription());
        indicationInfo.setRef(humanTaskDto.getAssignee());
        indicationInfo.setRelatedTaskId(delegateTask.getId());
        indicationInfo.setRelatedTaskStartTime(humanTaskDto.getCreateTime());
        indicationInfo.setRelatedTaskEndTime(humanTaskDto.getCompleteTime());
        indicationInfo.setInstanceId(humanTaskDto.getProcessInstanceId());
        return indicationInfo;
    }

    protected String getAssigneeByTaskID(String taskID) {
        HumanTaskDTO humanTaskDto = humanTaskConnector
                .findHumanTaskByTaskId(taskID);
        return humanTaskDto.getAssignee();
    }
}
