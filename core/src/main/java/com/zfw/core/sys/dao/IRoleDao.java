package com.zfw.core.sys.dao;

import com.zfw.core.dao.ICommonDao;
import com.zfw.core.sys.entity.Role;

public interface IRoleDao extends ICommonDao<Role,Integer> {

    boolean existsByName(String name);
    boolean existsByCode(String code);

    Role findByCode(String code);

    Role findByName(String name);
}
