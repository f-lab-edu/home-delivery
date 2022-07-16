package com.flab.delivery.utils;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordEncoder {

    /**
     * 인스턴스 생성 방지
     */
    private PasswordEncoder() {
    }

    public static String encode(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public static boolean matches(String password, String hashValue) {
        return BCrypt.checkpw(password, hashValue);
    }
}
