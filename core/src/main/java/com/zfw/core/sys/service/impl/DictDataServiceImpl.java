package com.zfw.core.sys.service.impl;
import com.zfw.core.annotation.cache.CacheAble;
import com.zfw.core.annotation.cache.CacheDisable;
import com.zfw.core.sys.dao.mapper.IDictMapperDao;
import com.zfw.core.sys.entity.DictData;
import com.zfw.core.sys.dao.IDictDataDao;
import com.zfw.core.sys.service.IDictDataService;
import com.zfw.core.service.impl.CommonServiceImpl;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.lang.String;


/**
* @Author:zfw
* @Date:2020-07-28
* @Content: 字典类型service实现类接口
*/
@Service
public class DictDataServiceImpl extends CommonServiceImpl<DictData,Integer> implements IDictDataService{

    @Autowired
    private IDictDataDao iDictDataDao;
    @Autowired
    private IDictMapperDao iDictMapperDao;

    @Override
    @CacheAble
    public List<DictData> findAll(Sort.Order... orders) {
        return super.findAll(orders);
    }

    @Override
    @CacheDisable
    public void deleteById(Integer integer) {
        super.deleteById(integer);
    }

    @Override
    @CacheAble
    public List<DictData> findByValueType(String valueType){return iDictDataDao.findByValueType(valueType);}
    @Override
    public boolean existsByValueType(String valueType){return iDictDataDao.existsByValueType(valueType);}

    @Override
    @CacheAble
    public List<DictData> findByTypeName(String typeName){return iDictDataDao.findByTypeName(typeName);}

    @Override
    @CacheAble
    public DictData findByTypeNameAndLabel(String typeName, String label) {
        return iDictDataDao.findByTypeNameAndLabel(typeName,label);
    }

    @Override
    @CacheAble
    public List<DictData> findByTypeNameOrderBySortAsc(String typeName) {
        return iDictDataDao.findByTypeNameOrderBySortAsc(typeName);
    }

    @Override
    @CacheDisable
    public DictData save(DictData dictData) {return super.save(dictData);}

    @Override
    public boolean existsByTypeName(String typeName){return iDictDataDao.existsByTypeName(typeName);}

    @Override
    @CacheAble
    public List<DictData> findByDescription(String description){return iDictDataDao.findByDescription(description);}
    @Override
    public boolean existsByDescription(String description){return iDictDataDao.existsByDescription(description);}

    @Override
    @CacheAble
    public List<DictData> findByLabel(String label){return iDictDataDao.findByLabel(label);}
    @Override
    public boolean existsByLabel(String label){return iDictDataDao.existsByLabel(label);}

    @Override
    @CacheAble
    public List<DictData> findByValue(String value){return iDictDataDao.findByValue(value);}
    @Override
    public boolean existsByValue(String value){return iDictDataDao.existsByValue(value);}

    @Override
    public List<DictData> findByIsSys(int isSys){return iDictDataDao.findByIsSys(isSys);}
    @Override
    public boolean existsByIsSys(int isSys){return iDictDataDao.existsByIsSys(isSys);}

    @Override
    @CacheAble
    public List<DictData> findByStatus(int status){return iDictDataDao.findByStatus(status);}
    @Override
    public boolean existsByStatus(int status){return iDictDataDao.existsByStatus(status);}

    @Override
    public boolean existsByValueAndTypeName(String value, String typeName,Integer id) {
        return iDictMapperDao.existsByValueAndTypeNameAndNotId(value,typeName,id);
    }

    @Override
    @CacheAble
    public DictData findByTypeNameAndValue(String typeName, String value) {
        return iDictDataDao.findByTypeNameAndValue(typeName,value);
    }
}

