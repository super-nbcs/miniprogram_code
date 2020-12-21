package com.zfw.core.sys.service;

import com.zfw.core.service.ICommonService;
import com.zfw.core.sys.entity.UserRole;

import java.util.List;
import java.util.Set;

/**
 * @Author:zfw
 * @Date:2019/8/5
 * @Content:
 */
public interface IUserRoleService extends ICommonService<UserRole,Integer> {
    /**
     * 绑定用户角色
     * @param userId
     * @param roleIds
     */
    void bindRole(Integer userId, List<Integer> roleIds);
    /**
     * 绑定用户角色
     * @param userId
     * @param roleId
     */
    void bindRole(Integer userId, Integer roleId);


    /**
     * 通过userId删除所有
     * @param userId
     */
    void deleteAllByUserId(Integer userId);

    /**
     * 通过roleId删除所有
     * @param roleId
     */
    void deleteAllByRoleId(Integer roleId);

    /**
     * 根据userId获取绑定的所有角色
     * @param userId
     * @return
     */
    Set<UserRole> findByUserId(Integer userId);

}
