package com.xunmaw.cms.module.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xunmaw.cms.module.admin.model.User;
import com.xunmaw.cms.module.admin.vo.UserOnlineVo;

import java.io.Serializable;
import java.util.List;

/**
 * @author Linzhaoguan
 * @version V1.0
 * @date 2019年9月11日
 */
public interface UserService extends IService<User> {

    /**
     * 根据用户名查询用户
     *
     * @param username
     * @return user
     */
    User selectByUsername(String username);

    /**
     * 注册用户
     *
     * @param user
     * @return int
     */
    int register(User user);

    /**
     * 更新最后登录时间
     *
     * @param user
     */
    void updateLastLoginTime(User user);

    /**
     * 根据条件查询用户列表
     *
     * @param user
     * @param pageNumber
     * @param pageSize
     * @return list
     */
    IPage<User> selectUsers(User user, Integer pageNumber, Integer pageSize);

    /**
     * 根据用户id查询用户
     *
     * @param userId
     * @return user
     */
    User selectByUserId(String userId);

    /**
     * 根据用户id更新用户信息
     *
     * @param user
     * @return int
     */
    int updateByUserId(User user);

    /**
     * 根据用户id集合批量更新用户状态
     *
     * @param userIds
     * @param status
     * @return int
     */
    boolean updateStatusBatch(List<String> userIds, Integer status);

    /**
     * 根据用户id分配角色集合
     *
     * @param userId
     * @param roleIds
     */
    void addAssignRole(String userId, List<String> roleIds);

    List<UserOnlineVo> selectOnlineUsers(UserOnlineVo userOnlineVo);


    void kickout(Serializable sessionId, String username);

}
