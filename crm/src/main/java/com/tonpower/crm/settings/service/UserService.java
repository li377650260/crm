package com.tonpower.crm.settings.service;

import com.tonpower.crm.exception.LoginException;
import com.tonpower.crm.settings.domain.User;

import java.util.List;

/**
 * @description:
 * @author: li377650260
 * @date: 2021/6/1 17:04
 */
public interface UserService {
    User login(String loginAct, String loginPwd, String ip) throws LoginException;

    List<User> getUserList();
}
