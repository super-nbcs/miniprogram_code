package com.zfw.core.sys.service;

import com.zfw.core.service.ICommonService;
import com.zfw.core.sys.entity.Button;
import com.zfw.core.sys.entity.RoleButton;

import java.util.List;
import java.util.Set;

public interface IRoleButtonService extends ICommonService<RoleButton,Integer> {

    /**
     * 绑定按钮权限
     * @param roleId    角色id
     * @param btnId    按钮id
     */
    void bindBtn(Integer roleId, Integer btnId);

    /**
     * 移除当前角色之前绑定的所有菜单权限，并重新 绑定菜单权限
     * @param roleId    角色id
     * @param btnIds   按钮ids
     */
    void bindBtn(Integer roleId, List<Integer> btnIds);

    /**
     * 通过roleId查询所有
     * @param roleId
     * @return
     */
    Set<RoleButton> findByRoleId(Integer roleId);

    /**
     * 通过btnId删除
     * @param btnId
     */
    void deleteAllByBtnId(Integer btnId);

    void deleteAllByRoleId(Integer roleId);

    List<Button> findBtnsByRoleId(int roleId);
}

