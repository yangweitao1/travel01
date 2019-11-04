package com.itheima.travel.service;

import com.itheima.travel.domain.User;

/**
 * 包名:com.itheima.travel.service
 * 作者:Leevi
 * 日期2019-07-16  08:40
 */
public interface UserService {
    User findUserByUsername(String username);

    void doRegister(User user) throws Exception;

    int doActive(String code);

    User doLogin(String username, String password);
}
