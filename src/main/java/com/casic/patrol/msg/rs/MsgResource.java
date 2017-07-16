package com.casic.patrol.msg.rs;


import com.casic.patrol.core.auth.CurrentUserHolder;
import com.casic.patrol.core.auth.MockCurrentUserHolder;
import com.casic.patrol.core.mapper.JsonMapper;
import com.casic.patrol.core.util.BaseDTO;
import com.casic.patrol.msg.persistence.manager.MsgInfoManager;
import com.casic.patrol.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Component
@Path("msg")
public class MsgResource {
    private static Logger logger = LoggerFactory.getLogger(MsgResource.class);
    private MsgInfoManager msgInfoManager;
    private JsonMapper jsonMapper = new JsonMapper();
    private boolean enable = true;

    @Autowired
    private HttpSession session;
    @GET
    @Path("unreadCount")
    @Produces(MediaType.APPLICATION_JSON)
    public BaseDTO unreadCount() {
        if (!enable) {
            return new BaseDTO();
        }

        CurrentUserHolder currentUserHolder = new MockCurrentUserHolder();
        Object userid = session.getAttribute(StringUtils.USERID);
        Object username = session.getAttribute(StringUtils.USERNAME);
        Integer count = 0;
        if (userid != null && username != null) {
            currentUserHolder.setUserId(userid.toString());
            currentUserHolder.setUsername(username.toString());
            String userId = currentUserHolder.getUserId();
            count = msgInfoManager.getCount(
                    "select count(*) from MsgInfo where receiverId=? and status=0",
                    userId);
        }

        BaseDTO result = new BaseDTO();
        result.setData(count);

        return result;
    }

    // ~ ======================================================================
    @Resource
    public void setMsgInfoManager(MsgInfoManager msgInfoManager) {
        this.msgInfoManager = msgInfoManager;
    }



    @Value("${msg.enable}")
    public void setEnable(boolean enable) {
        this.enable = enable;
    }
}
