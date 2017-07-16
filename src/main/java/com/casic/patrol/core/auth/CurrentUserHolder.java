package com.casic.patrol.core.auth;

public interface CurrentUserHolder {

     String getUserId();

     void setUserId(String userId);

     String getUsername();

     void setUsername(String username) ;
}
