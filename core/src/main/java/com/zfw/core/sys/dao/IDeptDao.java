package com.zfw.core.sys.dao;


import com.zfw.core.dao.ICommonDao;
import com.zfw.core.sys.entity.Dept;

import java.util.List;

public interface IDeptDao extends ICommonDao<Dept,Integer> {
    boolean existsByName(String name);

    Dept findByName(String name);

    List<Dept> findByParentIdOrderBySort(Integer parentId);

    List<Dept> findByFlag(Integer flag);

    List<Dept> findByFlagOrderBySort(Integer flag);

    List<Dept> findByCodeStartsWithAndFlagOrderBySort(String code,Integer flag);

    Dept findByCode(String code);

    Dept findTopByCode(String code);
    Dept findTop1ByPath(String path);

    boolean existsByPath(String path);
}
