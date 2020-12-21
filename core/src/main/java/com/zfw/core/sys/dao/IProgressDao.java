package com.zfw.core.sys.dao;

import com.zfw.core.dao.ICommonDao;
import com.zfw.core.sys.entity.Progress;

/**
 * @Author:yzh
 * @Date:2020/06/23
 * @Content:    进度条dao
 */
public interface IProgressDao extends ICommonDao<Progress,Integer> {
    Progress findTopByProgressFlag(String progressFlag);
}
