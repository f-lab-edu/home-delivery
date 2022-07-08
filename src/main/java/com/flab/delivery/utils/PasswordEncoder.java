package com.flab.delivery.utils;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordEncoder {

    public static String encrypt(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public static boolean isMatch(String password, String hashedPassword) {
        return BCrypt.checkpw(password, hashedPassword);
    }

}
