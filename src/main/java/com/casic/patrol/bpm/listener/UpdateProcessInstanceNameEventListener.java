package com.casic.patrol.bpm.listener;


import com.casic.patrol.api.user.UserConnector;
import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.delegate.event.BaseEntityEventListener;
import org.activiti.engine.delegate.event.impl.ActivitiEntityEventImpl;
import org.activiti.engine.impl.cmd.GetDeploymentProcessDefinitionCmd;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.HistoricProcessInstanceEntity;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UpdateProcessInstanceNameEventListener extends
        BaseEntityEventListener {
    private UserConnector userConnector;
    private String processNameFirstPart;

    protected void onInitialized(ActivitiEvent event) {
        if (!(event instanceof ActivitiEntityEventImpl)) {
            return;
        }

        ActivitiEntityEventImpl activitiEntityEventImpl = (ActivitiEntityEventImpl) event;
        Object entity = activitiEntityEventImpl.getEntity();

        if (!(entity instanceof ExecutionEntity)) {
            return;
        }

        ExecutionEntity executionEntity = (ExecutionEntity) entity;

        if (!executionEntity.isProcessInstanceType()) {
            return;
        }

        String processInstanceId = executionEntity.getId();
        String processDefinitionId = executionEntity.getProcessDefinitionId();
        CommandContext commandContext = Context.getCommandContext();
        ProcessDefinitionEntity processDefinition = new GetDeploymentProcessDefinitionCmd(
                processDefinitionId).execute(commandContext);

        // {流程标题:title}-{发起人:startUser}-{发起时间:startTime}
        String processDefinitionName = processDefinition.getName();
        if (processNameFirstPart != null &&
                executionEntity.getVariableInstances().containsKey(processNameFirstPart)) {
            processDefinitionName = executionEntity.getVariableInstances().get(processNameFirstPart).getTextValue();
        }
        String userId = Authentication.getAuthenticatedUserId();
        String displayName = userConnector.findById(userId).getDisplayName();
        String processInstanceName = processDefinitionName + "-" + displayName
                + "-"
                + new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date());
        // runtime
        executionEntity.setName(processInstanceName);

        // history
        HistoricProcessInstanceEntity historicProcessInstanceEntity = commandContext
                .getHistoricProcessInstanceEntityManager()
                .findHistoricProcessInstance(processInstanceId);
        historicProcessInstanceEntity.setName(processInstanceName);
    }

    @Resource
    public void setUserConnector(UserConnector userConnector) {
        this.userConnector = userConnector;
    }

    @Value("${jsonvalue.process.name.first.part}")
    public void setProcessNameFirstPart(String temp) {
        this.processNameFirstPart = temp;
    }
}
