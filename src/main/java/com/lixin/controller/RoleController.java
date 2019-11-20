package com.lixin.controller;


import com.lixin.domain.Role;
import com.lixin.domain.User;
import com.lixin.service.RoleService;
import com.poly.common.core.convert.DataResult;
import com.poly.common.core.convert.DataResultBuild;
import io.swagger.annotations.ApiOperation;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * <p>
 * 角色表 前端控制器
 * </p>
 *
 * @author zlb
 * @since 2019-10-12
 */
@RestController
@RequestMapping("/role")
public class RoleController {


    @Autowired
    private RoleService roleService;

    @ApiOperation("新增角色")
    @PostMapping("/add")
    public DataResult<String> addRole(@RequestBody Role role){
        ContextLoader.getCurrentWebApplicationContext();
        return roleService.add(role)? DataResultBuild.success("新增成功"):DataResultBuild.fail("新增失败");
    }

    @ApiOperation("查询所有角色")
    @GetMapping("/select")
    public DataResult<List<Role>> select(@RequestParam(required = false) String keyword){
        return roleService.select(keyword);
    }

    public static void main(String[] args) {

        Role role1 = new Role();
        role1.setRoleName("admin");
        role1.setRoleDesc("管理员");
        Role role = new Role();
        role.setRoleName("admin");
        role.setRoleDesc("管理员");
        Map<Role,String> map = new HashMap<>(16);
        map.put(role,"角色");
        map.put(role1,"角色1");
        System.out.println(map.size());
    }



}

