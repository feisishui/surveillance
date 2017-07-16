package com.casic.patrol.internal.sendmail.support;

import com.casic.patrol.api.notification.NotificationDTO;
import com.casic.patrol.api.notification.NotificationHandler;
import com.casic.patrol.api.user.UserConnector;
import com.casic.patrol.internal.sendmail.service.SendmailDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;

public class SendmailNotificationHandler implements NotificationHandler {
    private static Logger logger = LoggerFactory
            .getLogger(SendmailNotificationHandler.class);
    private SendmailDataService sendmailDataService;
    private UserConnector userConnector;

    public void handle(NotificationDTO notificationDto, String tenantId) {
        String email = null;

        if ("userid".equals(notificationDto.getReceiverType())) {
            email = userConnector.findById(notificationDto.getReceiver())
                    .getEmail();
        } else if ("email".equals(notificationDto.getReceiverType())) {
            email = notificationDto.getReceiver();
        } else {
            return;
        }

        try {
            sendmailDataService.send(email, notificationDto.getSubject(),
                    notificationDto.getContent(), "1", tenantId);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
    }

    public String getType() {
        return "sendmail";
    }

    @Resource
    public void setSendmailDataService(SendmailDataService sendmailDataService) {
        this.sendmailDataService = sendmailDataService;
    }

    @Resource
    public void setUserConnector(UserConnector userConnector) {
        this.userConnector = userConnector;
    }
}
