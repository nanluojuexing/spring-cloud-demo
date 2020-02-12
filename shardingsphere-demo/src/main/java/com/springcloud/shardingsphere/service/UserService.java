package com.springcloud.shardingsphere.service;

import com.springcloud.shardingsphere.entity.User;

import java.util.List;

public interface UserService {

    int saveOne(User user);

    List<User> list();
}
