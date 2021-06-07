package com.tonpower.crm.settings.service.impl;

import com.tonpower.crm.exception.LoginException;
import com.tonpower.crm.settings.dao.UserDao;
import com.tonpower.crm.settings.domain.User;
import com.tonpower.crm.settings.service.UserService;
import com.tonpower.crm.utils.DateTimeUtil;
import com.tonpower.crm.utils.SqlSessionUtil;
import org.apache.ibatis.session.SqlSession;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: li377650260
 * @date: 2021/6/1 17:05
 */
public class UserServiceImpl implements UserService {
     UserDao userDao = SqlSessionUtil.getSqlSession().getMapper(UserDao.class);

    @Override
    public User login(String loginAct, String loginPwd, String ip) throws LoginException {

        Map<String,String> map = new HashMap<>();
        map.put("loginAct",loginAct);
        map.put("loginPwd",loginPwd);
        User user = userDao.login(map);

        if (user == null){
            throw new LoginException("账号密码错误");
        }

        // 如果程序执行到此处说明账号密码正确，需要继续验证其他用户信息

        // 验证失效时间
        String expireTime = user.getExpireTime();
        String nowTime = DateTimeUtil.getSysTime();

        if (expireTime.compareTo(nowTime) < 0){
            throw new LoginException("账号已经失效");
        }

        // 判断锁定状态
        String lockState = user.getLockState();
        if ("0".equals(lockState)){
            throw new LoginException("此账号被锁定");
        }

        // 判断ip地址
        String allowIps = user.getAllowIps();
        if (allowIps != null && allowIps != "") {
            if (!allowIps.contains(ip)) {
                throw new LoginException("ip地址受限");
            }
        }
        return user;
    }

    @Override
    public List<User> getUserList() {
        List<User> list = userDao.getUserList();

        return list;
    }
}
