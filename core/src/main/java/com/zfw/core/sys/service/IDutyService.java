package com.zfw.core.sys.service;

import com.zfw.core.service.ICommonService;
import com.zfw.core.sys.entity.Duty;

public interface IDutyService extends ICommonService<Duty, Integer> {

    /**
     * 添加职位信息
     */
    Duty addDuty(Duty duty);

    /**
     * 修改职位信息
     */
    Duty updateDuty(Duty duty);

    /**
     * 删除职位信息
     * @param id
     */
    void deleteDuty(Integer id);

    /**
     * 根据id获取职位信息
     * @param id
     * @return
     */
    Duty findDutyById(Integer id);

}
