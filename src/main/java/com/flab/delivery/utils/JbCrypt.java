package com.flab.delivery.utils;

import org.mindrot.jbcrypt.BCrypt;

public class JbCrypt implements PasswordEncoder {
    @Override
    public String encoder(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    @Override
    public boolean isMatch(String password, String hashValue) {
        return BCrypt.checkpw(password, hashValue);
    }
}
