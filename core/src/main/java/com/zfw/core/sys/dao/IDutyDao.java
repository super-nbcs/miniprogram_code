package com.zfw.core.sys.dao;

import com.zfw.core.dao.ICommonDao;
import com.zfw.core.sys.entity.Duty;

public interface IDutyDao extends ICommonDao<Duty, Integer> {

    /**
     * 根据职位名称查找信息
     * @param name
     * @return
     */
    Duty findByName(String name);

    /**
     * 职位是否存在
     * @param name
     * @return
     */
    boolean existsByName(String name);
}
