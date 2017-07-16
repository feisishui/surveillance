package com.casic.patrol.android.rs;


import com.casic.patrol.api.tenant.TenantHolder;
import com.casic.patrol.api.user.UserConnector;
import com.casic.patrol.api.user.UserDTO;
import com.casic.patrol.core.mapper.JsonMapper;
import com.casic.patrol.core.util.BaseDTO;
import com.casic.patrol.msg.persistence.manager.MsgInfoManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.Map;

@Component
@Path("android/user")
public class AndroidUserResource {
    private static Logger logger = LoggerFactory
            .getLogger(AndroidUserResource.class);
    private JsonMapper jsonMapper = new JsonMapper();
    private TenantHolder tenantHolder;
    private MsgInfoManager msgInfoManager;
    private UserConnector userConnector;

    @POST
    @Path("profile")
    @Produces(MediaType.APPLICATION_JSON)
    public BaseDTO profile(@FormParam("userId") String userId)
            throws Exception {

        UserDTO userDto = userConnector.findById(userId);

        Map<String, String> map = new HashMap<String, String>();
        map.put("username", userDto.getUsername());
        map.put("displayName", userDto.getDisplayName());
        map.put("email", userDto.getEmail());
        map.put("mobile", userDto.getMobile());

        String json = jsonMapper.toJson(map);
        BaseDTO result = new BaseDTO();
        result.setCode(200);
        result.setData(json);
        logger.info("end");

        return result;
    }

    // ~ ======================================================================
    @Resource
    public void setMsgInfoManager(MsgInfoManager msgInfoManager) {
        this.msgInfoManager = msgInfoManager;
    }

    @Resource
    public void setTenantHolder(TenantHolder tenantHolder) {
        this.tenantHolder = tenantHolder;
    }

    @Resource
    public void setUserConnector(UserConnector userConnector) {
        this.userConnector = userConnector;
    }

}
