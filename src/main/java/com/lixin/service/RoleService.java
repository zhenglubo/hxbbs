package com.lixin.service;

import com.lixin.domain.Role;
import com.poly.common.core.convert.DataResult;

import java.util.List;

/**
 * <p>
 * 角色表 服务类
 * </p>
 *
 * @author zlb
 * @since 2019-10-12
 */
public interface RoleService{

    /**
     * 新增角色
     * @param role
     * @return
     */
    boolean add(Role role);


    /**
     * 查询
     * @param keyword
     * @return
     */
    DataResult<List<Role>> select(String keyword);
}
