package com.casic.patrol.core.auth;

public interface CustomPasswordEncoder {
    String encode(CharSequence rawPassword);

    boolean matches(CharSequence rawPassword, String encodedPassword);
}
