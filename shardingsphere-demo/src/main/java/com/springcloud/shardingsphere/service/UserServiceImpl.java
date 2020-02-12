package com.springcloud.shardingsphere.service;

import com.springcloud.shardingsphere.entity.User;
import com.springcloud.shardingsphere.entity.UserExample;
import com.springcloud.shardingsphere.mapper.UserMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;


    @Override
    public int saveOne(User user) {
        return userMapper.insertSelective(user);
    }

    @Override
    public List<User> list() {
        return userMapper.selectByExample(new UserExample());
    }
}
