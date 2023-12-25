package com.xunmaw.cms.module.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xunmaw.cms.module.admin.model.Permission;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * @author Linzhaoguan
 * @version V1.0
 * @date 2019年9月11日
 */
public interface PermissionMapper extends BaseMapper<Permission> {

    /**
     * 根据状态查询全部资源
     *
     * @param status 状态
     * @return the list
     */
    List<Permission> selectAllPerms(Integer status);

    /**
     * 根据状态查询全部菜单
     *
     * @param status 状态
     * @return the list
     */
    List<Permission> selectAllMenuName(Integer status);

    /**
     * 根据用户id查询权限集合
     *
     * @param userId 状态
     * @return set
     */
    Set<String> findPermsByUserId(String userId);

    /**
     * 根据角色id查询权限
     *
     * @param id 角色id
     * @return the list
     */
    List<Permission> findByRoleId(String id);

    /**
     * 根据用户id查询权限
     *
     * @param userId 用户id
     * @return the list
     */
    List<Permission> selectByUserId(String userId);

    /**
     * 根据用户id查询菜单
     *
     * @param userId 用户id
     * @return the list
     */
    List<Permission> selectMenuByUserId(String userId);

    /**
     * 根据权限id修改状态
     *
     * @param permissionId 权限id
     * @param status       状态
     * @return int
     */
    int updateStatusByPermissionId(@Param("permissionId") String permissionId, @Param("status") Integer status);

    /**
     * 根据权限id查询权限
     *
     * @param permissionId 权限id
     * @return permission
     */
    Permission selectByPermissionId(String permissionId);

    /**
     * 根据权限bean修改权限
     *
     * @param permission 权限
     * @return int
     */
    int updateByPermissionId(Permission permission);

    /**
     * 根据权限id查询有几个子资源
     *
     * @param permissionId 权限id
     * @return int
     */
    int selectSubPermsByPermissionId(String permissionId);
}