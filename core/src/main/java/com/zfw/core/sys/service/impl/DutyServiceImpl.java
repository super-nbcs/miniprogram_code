package com.zfw.core.sys.service.impl;

import com.zfw.core.annotation.cache.CacheAble;
import com.zfw.core.annotation.cache.CacheDisable;
import com.zfw.core.service.impl.CommonServiceImpl;
import com.zfw.core.sys.entity.Duty;
import com.zfw.core.sys.service.IDutyService;
import org.springframework.stereotype.Service;

@Service
public class DutyServiceImpl extends CommonServiceImpl<Duty, Integer> implements IDutyService {
    @Override
    @CacheDisable
    public Duty addDuty(Duty duty) {
        return this.save(duty);
    }

    @Override
    @CacheDisable
    public Duty updateDuty(Duty duty) {
        return this.save(duty);
    }

    @Override
    @CacheDisable
    public void deleteDuty(Integer id) {
        deleteById(id);
    }

    @Override
    @CacheAble
    public Duty findDutyById(Integer id) {
        Duty duty = this.getById(id);
        return duty;
    }
}
