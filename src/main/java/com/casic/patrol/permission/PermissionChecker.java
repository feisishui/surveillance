package com.casic.patrol.permission;

import com.casic.patrol.user.domain.User;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

public class PermissionChecker
{
    private PermissionMatcher permissionMatcher = new PermissionMatcher();

    public boolean isAuthorized(String text,User userObj) {

        List<String> haves = new ArrayList<String>();
        if (userObj == null ||userObj.getRole()==null)
        {
            return false;
        }
        String authorities = userObj.getRole().getFunctions();
        if (authorities == null)
            return false;
        for (String au : authorities.split(",")) {
            haves.add(au);
        }

        for (String want : text.split(",")) {
            for (String have :haves) {
                if (this.permissionMatcher.match(want, have)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void setReadOnly(boolean readOnly) {
        this.permissionMatcher.setReadOnly(readOnly);
    }

    public boolean isReadOnly() {
        return this.permissionMatcher.isReadOnly();
    }
}