package com.zfw.core.sys.dao;
import com.zfw.core.dao.ICommonDao;
import com.zfw.core.sys.entity.DictType;
import java.util.List;
import java.lang.String;
import java.lang.Integer;

/**
* @Author: zfw
* @Date: 2020-07-28
* @Content: 字典类型
*/
public interface IDictTypeDao extends ICommonDao<DictType,Integer> {

    List<DictType> findByTypeName(String typeName);

    boolean existsByTypeName(String typeName);

    List<DictType> findByDescription(String description);

    boolean existsByDescription(String description);

    List<DictType> findByStatus(Integer status);

    boolean existsByStatus(Integer status);
}
