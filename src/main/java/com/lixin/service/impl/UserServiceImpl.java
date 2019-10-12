package com.lixin.service.impl;

import com.lixin.domain.User;
import com.lixin.mapper.UserMapper;
import com.lixin.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author zlb
 * @since 2019-10-12
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
