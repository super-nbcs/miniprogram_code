package com.zfw.core.sys.service;
import com.zfw.core.service.ICommonService;
import com.zfw.core.sys.entity.DictType;
import java.util.List;
import java.lang.String;
import java.lang.Integer;

/**
* @Author:zfw
* @Date:2020-07-28
* @Content: 字典类型Service接口
*/
public interface IDictTypeService extends ICommonService<DictType,Integer> {

    List<DictType> findByTypeName(String typeName);
    boolean existsByTypeName(String typeName);

    List<DictType> findByDescription(String description);
    boolean existsByDescription(String description);

    List<DictType> findByStatus(Integer status);
    boolean existsByStatus(Integer status);
}

