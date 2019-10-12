package com.lixin.service.impl;

import com.lixin.domain.Role;
import com.lixin.mapper.RoleMapper;
import com.lixin.service.RoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 角色表 服务实现类
 * </p>
 *
 * @author zlb
 * @since 2019-10-12
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

}
