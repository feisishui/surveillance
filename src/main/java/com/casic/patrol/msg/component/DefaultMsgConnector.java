package com.casic.patrol.msg.component;


import com.casic.patrol.api.msg.MsgConnector;
import com.casic.patrol.msg.persistence.domain.MsgInfo;
import com.casic.patrol.msg.persistence.manager.MsgInfoManager;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

@Component
public class DefaultMsgConnector implements MsgConnector {
    private MsgInfoManager msgInfoManager;

    public void send(String subject, String content, String receiverId,
            String senderId) {
        MsgInfo msgInfo = new MsgInfo();
        msgInfo.setName(subject);
        msgInfo.setContent(content);
        msgInfo.setReceiverId(receiverId);
        msgInfo.setSenderId(senderId);
        msgInfo.setCreateTime(new Date());
        msgInfo.setStatus(0);
        msgInfoManager.save(msgInfo);
    }

    @Resource
    public void setMsgInfoManager(MsgInfoManager msgInfoManager) {
        this.msgInfoManager = msgInfoManager;
    }
}
