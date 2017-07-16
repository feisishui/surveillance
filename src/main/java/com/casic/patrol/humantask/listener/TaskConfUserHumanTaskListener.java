package com.casic.patrol.humantask.listener;


import com.casic.patrol.api.humantask.HumanTaskConstants;
import com.casic.patrol.humantask.persistence.domain.TaskInfo;
import com.casic.patrol.humantask.persistence.manager.TaskParticipantManager;
import com.casic.patrol.spi.humantask.TaskDefinitionConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;

public class TaskConfUserHumanTaskListener implements HumanTaskListener {
    private static Logger logger = LoggerFactory
            .getLogger(TaskConfUserHumanTaskListener.class);
    private TaskDefinitionConnector taskDefinitionConnector;
    private TaskParticipantManager taskParticipantManager;

    @Override
    public void onCreate(TaskInfo taskInfo) throws Exception {
        if (HumanTaskConstants.CATALOG_COPY.equals(taskInfo.getCatalog())) {
            return;
        }

        String taskDefinitionKey = taskInfo.getCode();
        String businessKey = taskInfo.getBusinessKey();
        String assignee = taskDefinitionConnector.findTaskConfUser(
                taskDefinitionKey, businessKey);

        if (assignee != null) {
            taskInfo.setAssignee(assignee);
        }
    }

    @Override
    public void onComplete(TaskInfo taskInfo) throws Exception {
    }

    @Resource
    public void setTaskDefinitionConnector(
            TaskDefinitionConnector taskDefinitionConnector) {
        this.taskDefinitionConnector = taskDefinitionConnector;
    }

    @Resource
    public void setTaskParticipantManager(
            TaskParticipantManager taskParticipantManager) {
        this.taskParticipantManager = taskParticipantManager;
    }
}
