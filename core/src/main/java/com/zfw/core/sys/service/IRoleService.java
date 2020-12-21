package com.zfw.core.sys.service;

import com.zfw.core.service.ICommonService;
import com.zfw.core.sys.entity.Role;

public interface IRoleService extends ICommonService<Role,Integer> {

    /**
     * 获取角色所具有的所有菜单权限
     * @param roleId
     * @return
     */
    Role getRoleMenus(Integer roleId);

    /**
     * name 是否存在
     * @param name
     * @return
     */
    boolean existsByName(String name);

    /**
     * code是否存在
     * @param code
     * @return
     */
    boolean existsByCode(String code);

    /**
     * 通过roleId删除role，并删除关联表role_menu中的role
     * @param id
     */
    void deleteRole(Integer id);

    /**
     * 通过角色代码获取角色
     * @param code
     * @return
     */
    Role findByCode(String code);


    Role findByName(String name);
}

