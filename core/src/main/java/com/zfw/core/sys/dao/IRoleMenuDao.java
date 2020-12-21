package com.zfw.core.sys.dao;


import com.zfw.core.dao.ICommonDao;
import com.zfw.core.sys.entity.RoleMenu;

import java.util.Set;

/**
 * @Author:zfw
 * @Date:2019/7/15
 * @Content:
 */
public interface IRoleMenuDao extends ICommonDao<RoleMenu, Integer> {
    Set<RoleMenu> findByRoleId(Integer roleId);
    RoleMenu findByRoleIdAndMenuId(Integer roleId, Integer menuId);

    void removeAllByRoleId(Integer roleId);

    void removeAllByMenuId(Integer menuId);
}
