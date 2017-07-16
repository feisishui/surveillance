package com.casic.patrol.api.user;

public interface AuthenticationHandler {
    boolean support(String type);

    String doAuthenticate(String username, String password, String application);
}
