package com.casic.patrol.user.dto;

import com.casic.patrol.user.domain.Role;
import com.casic.patrol.user.domain.User;
import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by lenovo on 2016/4/20.
 */
public class UserDto {

    private Long id;
    private String userName;
    private String password;
    private String sex;
    private String phoneNumber;
    private Integer isValid;
    private Long roleId;
    private String roleName;
    private Integer roleType;

    private String btnEdit = "<a href='#' class='btn mini blue'><i class='icon-edit'></i>编辑</a>";
    private String btnDelete = "<a href='#' class='btn mini red'><i class='icon-trash'></i>删除</a>";

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Integer getIsValid() {
        return isValid;
    }

    public void setIsValid(Integer isValid) {
        this.isValid = isValid;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Integer getRoleType() {
        return roleType;
    }

    public void setRoleType(Integer roleType) {
        this.roleType = roleType;
    }

    public static UserDto Convert(User user) {
        if (user == null) {
            return null;
        }
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setUserName(user.getUserName());
        userDto.setPassword(user.getPassword());
        userDto.setSex(user.getSex());
        userDto.setPhoneNumber(user.getPhoneNumber());
        userDto.setIsValid(user.getIsValid());
        Role role = user.getRole();
        if (role != null) {
            userDto.setRoleId(role.getId());
            userDto.setRoleName(role.getRoleName());
            userDto.setRoleType(role.getType());
        }
        return userDto;
    }

    public static List<UserDto> Converts(List<User> users) {
        if (CollectionUtils.isEmpty(users)) {
            return Collections.emptyList();
        }
        List<UserDto> userDtos = new ArrayList<UserDto>();
        for (User user : users) {
            userDtos.add(UserDto.Convert(user));
        }
        return userDtos;
    }
}
