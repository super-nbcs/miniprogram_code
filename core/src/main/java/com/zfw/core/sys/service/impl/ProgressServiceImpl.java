package com.zfw.core.sys.service.impl;

import com.zfw.core.service.impl.CommonServiceImpl;
import com.zfw.core.sys.dao.IProgressDao;
import com.zfw.core.sys.entity.Progress;
import com.zfw.core.sys.service.IProgressService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ProgressServiceImpl extends CommonServiceImpl<Progress, Integer> implements IProgressService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private IProgressDao iProgressDao;

    @Override
    public Progress findTopByProgressFlag(String progressFlag) {
        return iProgressDao.findTopByProgressFlag(progressFlag);
    }
}
