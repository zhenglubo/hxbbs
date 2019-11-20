package com.lixin.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @description: 角色分页查询dto对象
 * @author: zhenglubo
 * @create: 2019-10-17 09:26
 **/

@Data
public class RolePageQueryDto {

    @ApiModelProperty(value = "搜索关键字", example = "admin", position = 1)
    private String name;
    @ApiModelProperty(value = "当前页", example = "1", position = 2)
    private int currentPage = 1;
    @ApiModelProperty(value = "分页大小", example = "10", position = 3)
    private int pageSize = 10;
}
