package com.flab.delivery.service;

public interface LoginService {

    void login(String id);

    void logout(String id);

    String getCurrentUserId();
}
