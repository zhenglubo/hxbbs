package com.lixin.service.impl;

import com.lixin.domain.Permission;
import com.lixin.mapper.PermissionMapper;
import com.lixin.service.PermissionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 权限表 服务实现类
 * </p>
 *
 * @author zlb
 * @since 2019-10-12
 */
@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements PermissionService {

}
