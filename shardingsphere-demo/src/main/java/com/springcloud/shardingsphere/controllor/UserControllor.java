package com.springcloud.shardingsphere.controllor;

import com.springcloud.shardingsphere.entity.User;
import com.springcloud.shardingsphere.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserControllor {

    @Autowired
    private UserService userService;

    /**
     * @Description: 保存用户
     */
    @PostMapping("save-user")
    public Object saveUser(User user) {
        return userService.saveOne(user);
    }

    /**
     * @Description: 获取用户列表
     */
    @GetMapping("list-user")
    public List<User> listUser() {
        return userService.list();
    }
}
