package com.zfw.core.sys.dao;
import com.zfw.core.dao.ICommonDao;
import com.zfw.core.sys.entity.DictData;
import java.util.List;
import java.lang.String;

/**
* @Author: zfw
* @Date: 2020-07-28
* @Content: 字典类型
*/
public interface IDictDataDao extends ICommonDao<DictData,Integer> {

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

    DictData findByTypeNameAndValue(String typeName,String value);

}
