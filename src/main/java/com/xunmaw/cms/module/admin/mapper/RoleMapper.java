package com.xunmaw.cms.module.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xunmaw.cms.module.admin.model.Role;
import org.apache.ibatis.annotations.Param;

import java.util.Map;
import java.util.Set;

/**
 * @author Linzhaoguan
 * @version V1.0
 * @date 2019年9月11日
 */
public interface RoleMapper extends BaseMapper<Role> {
    /**
     * 根据用户id查询角色集合
     *
     * @param userId 用户id
     * @return set
     */
    Set<String> findRoleByUserId(String userId);

    /**
     * 根据role参数查询角色列表
     *
     * @param page
     * @param role role
     * @return list
     */
    IPage<Role> selectRoles(@Param("page") IPage<Role> page, @Param("role") Role role);

    /**
     * 根据参数批量更新状态
     *
     * @param params
     * @return int
     */
    int updateStatusBatch(Map<String, Object> params);

    /**
     * 根据roleId更新角色信息
     *
     * @param params
     * @return int
     */
    int updateByRoleId(Map<String, Object> params);


}