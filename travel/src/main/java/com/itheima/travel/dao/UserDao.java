package com.itheima.travel.dao;

import com.itheima.travel.domain.User;

/**
 * 包名:com.itheima.travel.dao
 * 作者:Leevi
 * 日期2019-07-16  08:40
 */
public interface UserDao {
    User findUserByUsername(String username);

    void saveUser(User user);

    User findUserByCode(String code);

    void updateUser(User user);
}
