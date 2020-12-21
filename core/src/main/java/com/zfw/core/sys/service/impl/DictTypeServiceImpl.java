package com.zfw.core.sys.service.impl;
import com.zfw.core.annotation.cache.CacheAble;
import com.zfw.core.annotation.cache.CacheDisable;
import com.zfw.core.sys.entity.DictType;
import com.zfw.core.sys.dao.IDictTypeDao;
import com.zfw.core.sys.service.IDictTypeService;
import com.zfw.core.service.impl.CommonServiceImpl;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.lang.String;
import java.lang.Integer;


/**
* @Author:zfw
* @Date:2020-07-28
* @Content: 字典类型service实现类接口
*/
@Service
public class DictTypeServiceImpl extends CommonServiceImpl<DictType,Integer> implements IDictTypeService{

    @Autowired
    private IDictTypeDao iDictTypeDao;

    @Override
    @CacheAble(ttl = 3600)
    public List<DictType> findAll(DictType dictType) {
        return super.findAll(dictType);
    }

    @Override
    @CacheAble()
    public List<DictType> findAll(Sort.Order... orders) {
        return super.findAll(orders);
    }

    @Override
    @CacheDisable
    public DictType save(DictType dictType) {
        return super.save(dictType);
    }

    @Override
    @CacheDisable
    public void deleteById(Integer integer) {super.deleteById(integer);}

    @Override
    @CacheAble
    public List<DictType> findByTypeName(String typeName){return iDictTypeDao.findByTypeName(typeName);}
    @Override
    public boolean existsByTypeName(String typeName){return iDictTypeDao.existsByTypeName(typeName);}

    @Override
    @CacheAble
    public List<DictType> findByDescription(String description){return iDictTypeDao.findByDescription(description);}
    @Override
    public boolean existsByDescription(String description){return iDictTypeDao.existsByDescription(description);}

    @Override
    @CacheAble
    public List<DictType> findByStatus(Integer status){return iDictTypeDao.findByStatus(status);}
    @Override
    public boolean existsByStatus(Integer status){return iDictTypeDao.existsByStatus(status);}
}

