package com.zfw.core.sys.service;
import com.zfw.core.service.ICommonService;
import com.zfw.core.sys.entity.DictData;
import java.util.List;
import java.lang.String;

/**
* @Author:zfw
* @Date:2020-07-28
* @Content: 字典类型Service接口
*/
public interface IDictDataService extends ICommonService<DictData,Integer> {

    List<DictData> findByValueType(String valueType);
    boolean existsByValueType(String valueType);

    List<DictData> findByTypeName(String typeName);
    DictData findByTypeNameAndLabel(String typeName,String label);
    List<DictData> findByTypeNameOrderBySortAsc(String typeName);

    boolean existsByTypeName(String typeName);

    List<DictData> findByDescription(String description);
    boolean existsByDescription(String description);

    List<DictData> findByLabel(String label);
    boolean existsByLabel(String label);

    List<DictData> findByValue(String value);
    boolean existsByValue(String value);

    List<DictData> findByIsSys(int isSys);
    boolean existsByIsSys(int isSys);

    List<DictData> findByStatus(int status);
    boolean existsByStatus(int status);
    boolean existsByValueAndTypeName(String value,String typeName,Integer id);
    DictData findByTypeNameAndValue(String typeName,String value);

}

