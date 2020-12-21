package com.zfw.core.sys.dao.mapper;

/**
 * @Author:zfw
 * @Date:2020/7/13
 * @Content:
 */

import com.zfw.core.sys.entity.Dept;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface IDeptMapperDao {

    @Select(value = "SELECT model.code,model.id FROM sys_dept as model where LENGTH(model.code)=#{codeLength} ORDER BY model.id desc LIMIT 1;")
    Dept findLastByCodeLength(int codeLength);

    @Select(value = "SELECT model.code,model.id FROM sys_dept as model where LENGTH(model.code)=#{codeLength} and path like CONCAT(#{parentName},'%')  ORDER BY model.id desc LIMIT 1;")
    Dept findLastByCodeLengthAndLikeParentName(@Param("codeLength") int codeLength,@Param("parentName") String parentName);

    @Select(value ="<script>"+
            "SELECT COUNT(1) FROM sys_dept as model where code like CONCAT(#{parentCode},'%') and name = #{name} " +
            "<if test='parentPath!=null'> and path = #{parentPath}</if> <if test='id!=null'> and id !=#{id}</if>;</script>")
    boolean existsByNameAndParentCode(@Param("name") String name,@Param("parentPath") String parentPath, @Param("parentCode") String code, @Param("id") Integer id);

    @Select("SELECT name FROM sys_dept where flag =3 GROUP BY name;")
    List<String> getAllGradeGroup();

}
