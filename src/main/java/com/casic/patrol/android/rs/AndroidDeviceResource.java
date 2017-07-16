package com.casic.patrol.android.rs;


import com.casic.patrol.api.user.UserConnector;
import com.casic.patrol.core.mapper.JsonMapper;
import com.casic.patrol.core.util.BaseDTO;
import com.casic.patrol.party.persistence.domain.PartyEntity;
import com.casic.patrol.party.persistence.domain.PartyStruct;
import com.casic.patrol.party.persistence.manager.PartyEntityManager;
import com.casic.patrol.party.persistence.manager.PartyStructManager;
import com.casic.patrol.user.domain.Role;
import com.casic.patrol.user.domain.User;
import com.casic.patrol.user.manager.UserManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Path("android")
public class AndroidDeviceResource {
    private static Logger logger = LoggerFactory
            .getLogger(AndroidDeviceResource.class);
    private UserConnector userConnector;
    @Resource
    private UserManager userManager;
    @Resource
    private PartyEntityManager partyEntityManager;
    @Resource
    private PartyStructManager partyStructManager;

    private JsonMapper jsonMapper = new JsonMapper();

    @POST
    @Path("login")
    @Produces(MediaType.APPLICATION_JSON)
    public BaseDTO login(@FormParam("username") String username,
                         @FormParam("password") String password) {

        User user0 = new User();
        user0.setUserName(username);
        user0.setPassword(password);
        User user = userManager.AppLogin(user0);
        if (user == null) {//登录失败

            BaseDTO result = new BaseDTO();
            result.setCode(400);
            result.setMessage("登录失败，用户名或密码错误");
            logger.info("username is null");
            return result;
        }
        BaseDTO result = new BaseDTO();
        result.setCode(200);
        result.setData(user.getId());
        return  result;

    }

//    @POST
//    @Path("getPatrolers")
//    @Produces(MediaType.APPLICATION_JSON)
//    public BaseDTO getPatrolersInfo(@FormParam("username") String username) {
//
//        Role role = new Role();
//        List<User> users = userManager.getPatrolers(role);
//        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
//        for (User user : users) {
//            Map<String, Object> map = new HashMap<String, Object>();
//            map.put("id", user.getId());
//            map.put("username", user.getUserName());
//            list.add(map);
//        }
//        BaseDTO result = null;
//        try {
//            String json = jsonMapper.toJson(list);
//            result = new BaseDTO();
//            result.setCode(200);
//            result.setData(json);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return  result;
//    }

    @POST
    @Path("getPatrolers")
    @Produces(MediaType.APPLICATION_JSON)
    public BaseDTO getNextUserInfo(@FormParam("username") String username) {

        Role role = new Role();

        PartyEntity partyEntity = partyEntityManager.getPartyEntityByName("指挥中心坐席");
        List<PartyEntity> partyEntities = partyStructManager.getChilePartiesByParent(partyEntity);
//        List<User> users = userManager.getPatrolers(role);
        List<User> users = userManager.getUserByParties(partyEntities);
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (User user : users) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("id", user.getId());
            map.put("username", user.getUserName());
            list.add(map);
        }
        BaseDTO result = null;
        try {
            String json = jsonMapper.toJson(list);
            result = new BaseDTO();
            result.setCode(200);
            result.setData(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  result;
    }
}
