package com.lixin.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lixin.domain.User;
import com.lixin.service.UserService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @description: 登陆控制
 * @author: zhenglubo
 * @create: 2019-10-12 11:17
 **/

@Api(tags = "登陆controller")
@Controller
public class Login {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String viewIndex() {
        return "login";
    }

    @GetMapping("/header")
    public String viewHead() {
        return "header";
    }

    @GetMapping("/footer")
    public String viewFoot() {
        return "footer";
    }

    @GetMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        queryWrapper.eq("password", password);
        User user = userService.getOne(queryWrapper);
        return user == null ? "login" : "index";
    }
}
