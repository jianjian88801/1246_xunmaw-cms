package com.xunmaw.cms.module.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xunmaw.cms.module.admin.model.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Linzhaoguan
 * @version V1.0
 * @date 2019年9月11日
 */
public interface UserMapper extends BaseMapper<User> {
    /**
     * 根据user参数查询用户列表
     *
     * @param page
     * @param user
     * @return list
     */
    IPage<User> selectUsers(@Param("page") IPage<User> page, @Param("user") User user);

    /**
     * 根据角色id查询用户list
     *
     * @param roleId
     * @return list
     */
    List<User> findByRoleId(String roleId);

    /**
     * 根据角色id查询用户list
     *
     * @param roleIds
     * @return list
     */
    List<User> findByRoleIds(List<String> roleIds);
}
