package com.lixin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lixin.domain.Role;
import com.lixin.enums.ErrorCode;
import com.lixin.mapper.RoleMapper;
import com.lixin.service.RoleService;
import com.lixin.utils.ElasticSearchService;
import com.poly.common.core.convert.DataResult;
import com.poly.common.core.convert.DataResultBuild;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.elasticsearch.action.index.IndexResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 角色表 服务实现类
 * </p>
 *
 * @author zlb
 * @since 2019-10-12
 */
@Service
@Slf4j
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private ElasticSearchService elasticSearchService;

    @Override
    public boolean add(Role role) {
        boolean result = roleMapper.add(role);
        if (result) {
            IndexResponse indexResponse = elasticSearchService.indexSingle("hxbbs", "role", role.getId().toString(), role);
            if (indexResponse == null || indexResponse.status().getStatus() != ErrorCode.OK.getCode()) {
                log.error("数据存入es失败 参数：{}", role.toString());
            }
        }
        return result;
    }

    @Override
    public DataResult<List<Role>> select(String keyword) {
        List<Role> roles;
        roles = elasticSearchService.searchWithIndices(Role.class, null, keyword, true, new String[]{"roleName", "roleDesc"});
        if (CollectionUtils.isEmpty(roles)) {
            QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
            queryWrapper.like("role_name", keyword);
            roles = roleMapper.selectList(queryWrapper);
        }
        log.info("共查询到{}条数据", roles.size());
        return DataResultBuild.success(roles);
    }
}
