package com.zfw.core.sys.dao;

import com.zfw.core.dao.ICommonDao;
import com.zfw.core.sys.entity.Menu;

import java.util.List;
import java.util.Set;

public interface IMenuDao extends ICommonDao<Menu,Integer> {

    Set<Menu> findAllByParentId(Integer parentId);

    List<Menu> findAllByParentIdOrderBySortAsc(Integer parentId);

    Menu findByName(String name);
}
