package com.zfw.core.sys.service;

import com.zfw.core.service.ICommonService;
import com.zfw.core.sys.entity.Menu;

import java.util.List;
import java.util.Set;

public interface IMenuService extends ICommonService<Menu,Integer> {
    Set<Menu> findByParentId(Integer parentId);

    List<Menu> findAllByParentIdOrderBySortAsc(Integer parentId);

    /**
     * 获取所有菜单，并以树型结构展示
     * @return
     */
    List<Menu> getAllTreeMenus();

    /**
     * 通过id 删除菜单，并将关联表role_menu中的菜单也删除
     * @param id
     */
    void deleteMenu(Integer id);

    Menu findByName(String name);
}

