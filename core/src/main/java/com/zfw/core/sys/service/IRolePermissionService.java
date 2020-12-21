package com.zfw.core.sys.service;

import com.zfw.core.service.ICommonService;
import com.zfw.core.sys.entity.RolePermission;

import java.util.Map;

public interface IRolePermissionService extends ICommonService<RolePermission, Integer> {

    /**
     * 角色绑定数据权限
     * @param flag: 0 绑定本人权限  1 绑定全部权限 2 自定义权限
     * @param roleId
     * @param deptIds
     * @return
     */
    RolePermission bindRolePermission(int flag, int roleId, String deptIds);

    /**
     * 创建角色-权限
     * @param roleId
     * @param deptIds
     * @return
     */
    RolePermission createRolePermission(int roleId, String deptIds);

    /**
     * 根据角色id删除绑定的数据权限
     * @param roleId
     */
    void deleteByRoleId(Integer roleId);

    /**
     * 根据角色id获取绑定的数据权限
     * @param roleId
     * @return
     */
    Map<String, Object> getPermissionByRoleId(Integer roleId);

    /**
     * 根据角色id查找绑定的数据权限
     * @param roleId
     * @return
     */
    RolePermission findByRoleId(Integer roleId);

}
