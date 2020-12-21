package com.zfw.core.sys.dao;

import com.zfw.core.dao.ICommonDao;
import com.zfw.core.sys.entity.RoleButton;

import java.util.Set;

/**
 * @Author:zfw
 * @Date:2019/7/15
 * @Content:
 */
public interface IRoleButtonDao extends ICommonDao<RoleButton, Integer> {
    Set<RoleButton> findByRoleId(Integer roleId);
    RoleButton findByRoleIdAndBtnId(Integer roleId, Integer btnId);

    void removeAllByRoleId(Integer roleId);

    void removeAllByBtnId(Integer btnId);
}
