package com.lixin.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @description: 登录dto对象
 * @author: zhenglubo
 * @create: 2019-11-07 15:42
 **/

@Data
public class LoginDto {

    @ApiModelProperty(value = "用户名",position = 1)
    @NotBlank
    private String username;
    @ApiModelProperty(value = "密码",position = 2)
    @NotBlank
    private String password;
}
