package com.flab.delivery.utils;

public interface PasswordEncoder {

    String encoder(String password);

    boolean isMatch(String password, String hashValue);
}
