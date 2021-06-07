package com.tonpower.crm.settings.dao;

import com.tonpower.crm.settings.domain.User;

import java.awt.*;
import java.util.List;
import java.util.Map;

public interface UserDao {

   public User login(Map<String, String> map);

   List<User> getUserList();
}
