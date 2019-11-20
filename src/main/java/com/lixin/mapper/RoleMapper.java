package com.lixin.mapper;

import com.lixin.domain.Role;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 角色表 Mapper 接口
 * </p>
 *
 * @author zlb
 * @since 2019-10-12
 */
public interface RoleMapper extends BaseMapper<Role> {

    /**
     * 新增
     * @param role
     * @return
     */
    boolean add(@Param(value = "role") Role role);
}
