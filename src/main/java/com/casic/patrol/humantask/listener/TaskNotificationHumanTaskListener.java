package com.casic.patrol.humantask.listener;


import com.casic.patrol.api.notification.NotificationConnector;
import com.casic.patrol.api.notification.NotificationDTO;
import com.casic.patrol.api.user.UserConnector;
import com.casic.patrol.api.user.UserDTO;
import com.casic.patrol.humantask.persistence.domain.TaskInfo;
import com.casic.patrol.spi.humantask.TaskDefinitionConnector;
import com.casic.patrol.spi.humantask.TaskNotificationDTO;
import com.casic.patrol.spi.process.InternalProcessConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskNotificationHumanTaskListener implements HumanTaskListener {
    private static Logger logger = LoggerFactory
            .getLogger(TaskNotificationHumanTaskListener.class);
    private TaskDefinitionConnector taskDefinitionConnector;
    private NotificationConnector notificationConnector;
    private UserConnector userConnector;
    private InternalProcessConnector internalProcessConnector;
    private String baseUrl;

    @Override
    public void onCreate(TaskInfo taskInfo) throws Exception {
        // TODO: ignore notification when skip this task
        this.doNotice(taskInfo, "create");
    }

    @Override
    public void onComplete(TaskInfo taskInfo) throws Exception {
        this.doNotice(taskInfo, "complete");
    }

    public void doNotice(TaskInfo taskInfo, String eventName) {
        String taskDefinitionKey = taskInfo.getCode();
        String processDefinitionId = taskInfo.getProcessDefinitionId();
        List<TaskNotificationDTO> taskNotifications = taskDefinitionConnector
                .findTaskNotifications(taskDefinitionKey, processDefinitionId,
                        eventName);

        Map<String, Object> data = this.prepareData(taskInfo);

        for (TaskNotificationDTO taskNotification : taskNotifications) {
            String templateCode = taskNotification.getTemplateCode();
            String type = taskNotification.getType();
            String receiver = taskNotification.getReceiver();
            UserDTO userDto = null;

            if ("任务接收人".equals(receiver)) {
                userDto = userConnector.findById(taskInfo.getAssignee());
            } else if ("流程发起人".equals(receiver)) {
                String initiator = internalProcessConnector
                        .findInitiator(taskInfo.getProcessInstanceId());
                userDto = userConnector.findById(initiator);
            } else {
                userDto = userConnector.findById(receiver);
            }

            if (userDto == null) {
                logger.debug("userDto is null : {}", receiver);

                continue;
            }

            NotificationDTO notificationDto = new NotificationDTO();
            notificationDto.setReceiver(userDto.getId());
            notificationDto.setReceiverType("userid");
            notificationDto.setTypes(Arrays.asList(type.split(",")));
            notificationDto.setData(data);
            notificationDto.setTemplate(templateCode);
            notificationConnector.send(notificationDto, taskInfo.getTenantId());
        }
    }

    public Map<String, Object> prepareData(TaskInfo taskInfo) {
        String assignee = taskInfo.getAssignee();
        String initiator = internalProcessConnector.findInitiator(taskInfo
                .getProcessInstanceId());
        UserDTO assigneeUser = null;

        if (assignee != null) {
            assigneeUser = userConnector.findById(assignee);
        }

        UserDTO initiatorUser = userConnector.findById(initiator);

        Map<String, Object> data = new HashMap<String, Object>();

        Map<String, Object> taskEntity = new HashMap<String, Object>();
        taskEntity.put("id", taskInfo.getId());
        taskEntity.put("name", taskInfo.getName());

        if (assigneeUser != null) {
            taskEntity.put("assignee", assigneeUser.getDisplayName());
        }

        data.put("task", taskEntity);
        data.put("initiator", initiatorUser.getDisplayName());
        data.put("humanTask", taskInfo);
        data.put("baseUrl", baseUrl);
        data.put("humanTaskId", Long.toString(taskInfo.getId()));

        return data;
    }

    @Resource
    public void setTaskDefinitionConnector(
            TaskDefinitionConnector taskDefinitionConnector) {
        this.taskDefinitionConnector = taskDefinitionConnector;
    }

    @Resource
    public void setNotificationConnector(
            NotificationConnector notificationConnector) {
        this.notificationConnector = notificationConnector;
    }

    @Resource
    public void setUserConnector(UserConnector userConnector) {
        this.userConnector = userConnector;
    }

    @Resource
    public void setInternalProcessConnector(
            InternalProcessConnector internalProcessConnector) {
        this.internalProcessConnector = internalProcessConnector;
    }

    @Value("${application.baseUrl}")
    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }
}
