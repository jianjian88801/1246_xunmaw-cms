package com.xunmaw.cms.module.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xunmaw.cms.module.admin.model.Permission;
import com.xunmaw.cms.module.admin.model.Role;
import com.xunmaw.cms.module.admin.model.User;

import java.util.List;
import java.util.Set;

/**
 * @author Linzhaoguan
 * @version V1.0
 * @date 2019年9月11日
 */
public interface RoleService extends IService<Role> {

    /**
     * 根据用户id查询角色集合
     *
     * @param userId
     * @return set
     */
    Set<String> findRoleByUserId(String userId);

    /**
     * 根据条件查询角色列表
     *
     * @param role
     * @param pageNumber
     * @param pageSize
     * @return list
     */
    IPage<Role> selectRoles(Role role, Integer pageNumber, Integer pageSize);

    /**
     * 插入角色
     *
     * @param role
     * @return int
     */
    int insert(Role role);

    /**
     * 批量更新状态
     *
     * @param roleIds
     * @param status
     * @return int
     */
    int updateStatusBatch(List<String> roleIds, Integer status);

    /**
     * 根据id查询角色
     *
     * @param roleId
     * @return role
     */
    Role findById(Integer roleId);

    /**
     * 根据角色id更新角色信息
     *
     * @param role
     * @return int
     */
    int updateByRoleId(Role role);

    /**
     * 根据角色id查询权限集合
     *
     * @param roleId
     * @return list
     */
    List<Permission> findPermissionsByRoleId(String roleId);

    /**
     * 根据角色id保存分配权限
     *
     * @param roleId
     * @param permissionIdsList
     */
    void addAssignPermission(String roleId, List<String> permissionIdsList);

    /**
     * 根据角色id下的所有用户
     *
     * @param roleId
     * @return list
     */
    List<User> findByRoleId(String roleId);

    /**
     * 根据角色id下的所有用户
     *
     * @param roleIds
     * @return list
     */
    List<User> findByRoleIds(List<String> roleIds);


}
