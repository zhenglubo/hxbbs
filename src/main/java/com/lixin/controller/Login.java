package com.lixin.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lixin.domain.User;
import com.lixin.dto.LoginDto;
import com.lixin.service.UserService;
import com.poly.common.core.convert.DataResult;
import com.poly.common.core.convert.DataResultBuild;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @description: 登陆控制
 * @author: zhenglubo
 * @create: 2019-10-12 11:17
 **/

@Api(tags = "登陆controller")
@RestController
@RequestMapping("login")
public class Login {

    @Autowired
    private UserService userService;

    @ApiOperation(value = "用户登录")
    @PostMapping("/login")
    public DataResult<Boolean> login(@Validated @RequestBody LoginDto reqParam) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", reqParam.getUsername());
        queryWrapper.eq("password", reqParam.getPassword());
        User user = userService.getOne(queryWrapper);
        return user != null ? DataResultBuild.success():DataResultBuild.fail("账号密码不存在");
    }
}
