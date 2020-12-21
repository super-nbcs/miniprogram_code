package com.zfw.core.sys.dao;

import com.zfw.core.dao.ICommonDao;
import com.zfw.core.sys.entity.UserRole;

import java.util.Set;

/**
 * @Author:zfw
 * @Date:2019/7/15
 * @Content:
 */
public interface IUserRoleDao extends ICommonDao<UserRole, Integer> {
    Set<UserRole> findByUserId(Integer userId);

    UserRole findTopByUserIdAndRoleId(Integer userId, Integer roleId);

    void removeAllByUserId(Integer userId);

    void removeAllByRoleId(Integer roleId);


}
