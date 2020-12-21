package com.zfw.core.sys.service.impl;

import com.zfw.core.annotation.cache.CacheAble;
import com.zfw.core.annotation.cache.CacheDisable;
import com.zfw.core.service.impl.CommonServiceImpl;
import com.zfw.core.sys.dao.IImportMsgDao;
import com.zfw.core.sys.entity.ImportMsg;
import com.zfw.core.sys.service.IImportMsgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author:zfw
 * @Date:2019/8/30
 * @Content:
 */
@Service
public class ImportMsgServiceImpl extends CommonServiceImpl<ImportMsg, Integer> implements IImportMsgService {

    @Autowired
    private IImportMsgDao iImportErrorMsgDao;


    @Override
    @CacheDisable
    public ImportMsg save(ImportMsg importMsg) {
        return super.save(importMsg);
    }

    @Override
    @CacheDisable
    public void deleteById(Integer integer) {
        super.deleteById(integer);
    }

    @Override
    @CacheAble
    public ImportMsg findByErrorFileName(String errorFileName) {
        return iImportErrorMsgDao.findByErrorFileName(errorFileName);
    }

    @Override
    @CacheAble
    public ImportMsg findByFileName(String fileName) {
        return iImportErrorMsgDao.findByFileName(fileName);
    }

    @Override
    @CacheAble
    public long countUntreated() {
        return iImportErrorMsgDao.countUntreated();
    }
}
