package com.zfw.core.sys.service;

import com.zfw.core.service.ICommonService;
import com.zfw.core.sys.entity.Progress;

/**
 * @Author:zfw
 * @Date:2020/6/23
 * @Content:
 */
public interface IProgressService extends ICommonService<Progress, Integer> {
    Progress findTopByProgressFlag(String progressFlag);
}
