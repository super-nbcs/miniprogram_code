package com.zfw.core.sys.service;

import com.zfw.core.service.ICommonService;
import com.zfw.core.sys.entity.RoleMenu;

import java.util.List;
import java.util.Set;

public interface IRoleMenuService extends ICommonService<RoleMenu,Integer> {

    /**
     * 绑定菜单权限
     * @param roleId    角色id
     * @param menuId    菜单id
     */
    void bindMenu(Integer roleId, Integer menuId);

    /**
     * 移除当前角色之前绑定的所有菜单权限，并重新 绑定菜单权限
     * @param roleId    角色id
     * @param menuIds   菜单ids
     */
    void bindMenu(Integer roleId, List<Integer> menuIds);

    /**
     * 通过roleId查询所有
     * @param roleId
     * @return
     */
    Set<RoleMenu> findByRoleId(Integer roleId);

    /**
     * 通过menuId删除
     * @param menuId
     */
    void deleteAllByMenuId(Integer menuId);

    void deleteAllByRoleId(Integer roleId);
    RoleMenu findByRoleIdAndMenuId(Integer roleId, Integer menuId);
}

