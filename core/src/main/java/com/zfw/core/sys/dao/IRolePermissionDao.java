package com.zfw.core.sys.dao;

import com.zfw.core.dao.ICommonDao;
import com.zfw.core.sys.entity.RolePermission;

public interface IRolePermissionDao extends ICommonDao<RolePermission, Integer> {

    /**
     * 根据角色id查找绑定的数据权限
     * @param roleId
     * @return
     */
    RolePermission findByRoleId(Integer roleId);

    /**
     * 根据角色id删除绑定的数据权限
     * @param roleId
     */
    void deleteByRoleId(Integer roleId);

}
