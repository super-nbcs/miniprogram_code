package com.zfw.core.sys.dao.mapper;

/**
 * @Author:zfw
 * @Date:2020/7/13
 * @Content:
 */

import com.zfw.core.sys.entity.DictData;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface IDictMapperDao {
    @Select("select * from sys_dict")
    @Results(id="dictMap",value={
            @Result(column = "id",property = "id"),
            @Result(column = "value",property = "value"),
            @Result(column = "label",property = "label"),
            @Result(column = "type",property = "type"),
            @Result(column = "description",property = "description"),
            @Result(column = "code",property = "code"),
            @Result(column = "is_sys",property = "isSys"),
            @Result(column = "create_by",property = "createBy"),
            @Result(column = "update_by",property = "updateBy"),
            @Result(column = "del_flag",property = "delFlag"),
            @Result(column = "create_date",property = "createDate"),
            @Result(column = "update_date",property = "updateDate"),
            @Result(column = "remarks",property = "remarks")
    })
    List<DictData> findAll();

    @Select(value = "SELECT model.* FROM sys_dict as model GROUP BY model.code;")
    @ResultMap("dictMap")
    List<DictData> groupByCode();

    @Select(value ="<script>"+
            "SELECT COUNT(1) FROM sys_dict_data as model where model.value =#{value} and model.type_name = #{typeName} " +
            "<if test='id!=null'> and model.id !=#{id}</if>;</script>")
    boolean existsByValueAndTypeNameAndNotId(@Param("value") String value, @Param("typeName") String typeName, @Param("id") Integer id);

}
