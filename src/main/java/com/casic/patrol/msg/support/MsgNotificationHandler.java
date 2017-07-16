package com.casic.patrol.msg.support;


import com.casic.patrol.api.notification.NotificationDTO;
import com.casic.patrol.api.notification.NotificationHandler;
import com.casic.patrol.msg.persistence.domain.MsgInfo;
import com.casic.patrol.msg.persistence.manager.MsgInfoManager;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Resource;
import java.util.Date;

public class MsgNotificationHandler implements NotificationHandler {
    private MsgInfoManager msgInfoManager;
    private String defaultSender = "";

    public void handle(NotificationDTO notificationDto, String tenantId) {
        if (!"userid".equals(notificationDto.getReceiverType())) {
            return;
        }

        MsgInfo msgInfo = new MsgInfo();
        msgInfo.setName(notificationDto.getSubject());
        msgInfo.setContent(notificationDto.getContent());
        msgInfo.setReceiverId(notificationDto.getReceiver());
        msgInfo.setCreateTime(new Date());
        msgInfo.setStatus(0);
        msgInfo.setTenantId(tenantId);

        String humanTaskId = (String) notificationDto.getData().get(
                "humanTaskId");
        msgInfo.setData(humanTaskId);

        if (StringUtils.isNotBlank(notificationDto.getSender())) {
            msgInfo.setSenderId(notificationDto.getSender());
        } else {
            msgInfo.setSenderId(defaultSender);
        }

        msgInfoManager.save(msgInfo);
    }

    public String getType() {
        return "msg";
    }

    public void setDefaultSender(String defaultSender) {
        this.defaultSender = defaultSender;
    }

    @Resource
    public void setMsgInfoManager(MsgInfoManager msgInfoManager) {
        this.msgInfoManager = msgInfoManager;
    }
}
