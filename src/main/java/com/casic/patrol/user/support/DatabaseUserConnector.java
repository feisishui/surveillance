package com.casic.patrol.user.support;


import com.casic.patrol.api.user.UserConnector;
import com.casic.patrol.api.user.UserDTO;
import com.casic.patrol.core.page.Page;
import com.casic.patrol.core.spring.ApplicationContextHelper;
import com.casic.patrol.user.domain.User;
import com.casic.patrol.user.manager.UserManager;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

public class DatabaseUserConnector implements UserConnector {

    protected UserDTO convertUserDTO(User user) {
        if (user == null) {
            return null;
        }

        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId().toString());
        userDTO.setUsername(user.getUserName());
        userDTO.setNickName(user.getUserName());
        userDTO.setDisplayName(user.getUserName());
        userDTO.setEmail("");
        userDTO.setMobile(user.getPhoneNumber());
        userDTO.setUserRepoRef("");
        userDTO.setStatus(user.getIsValid());
        return userDTO;
    }

    public UserManager getUserManager() {
            return ApplicationContextHelper.getBean(UserManager.class);
        }

    public UserDTO findById(String id) {

        User user = getUserManager().get(Long.parseLong(id));
        if (user == null) {
            return null;
        }
        return convertUserDTO(user);
    }

    public UserDTO findByUsername(String username, String userRepoRef) {

        String hql = " from User u where u.userName=?";
        List<User> users = getUserManager()
                .find(hql, username);
        for (User user : users) {
            return convertUserDTO(user);
        }
        return null;
    }

    public UserDTO findByRef(String ref, String userRepoRef) {
        return null;    }

    public Page pagedQuery(String userRepoRef, Page page, Map<String, Object> parameters) {
        return null;
    }

    public UserDTO findByNickName(String nickName, String userRepoRef) {
        String hql = " from User u where u.userName=?";
        List<User> users = getUserManager()
                .find(hql, nickName);
        for (User user : users) {
            return convertUserDTO(user);
        }
        return null;
    }
}
