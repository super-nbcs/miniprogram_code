package com.zfw.core.sys.dao.mapper;

import com.zfw.core.sys.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @Author:zfw
 * @Date:2020/6/12
 * @Content:
 */
@Mapper
public interface IUserMapperDao {
    @Select("select * from sys_user")
    @Results(id="userMap",value={
            @Result(column = "id",property = "id"),
            @Result(column = "user_name",property = "userName"),
            @Result(column = "nick_name",property = "nickName"),
            @Result(column = "name",property = "name"),
            @Result(column = "id_card",property = "idCard"),
            @Result(column = "front_photo",property = "frontPhoto"),
            @Result(column = "back_photo",property = "backPhoto"),
            @Result(column = "residence_address",property = "residenceAddress"),
            @Result(column = "gender",property = "gender"),
            @Result(column = "phone",property = "phone"),
            @Result(column = "email",property = "email"),
            @Result(column = "photo",property = "photo"),
            @Result(column = "open_id",property = "openId"),
            @Result(column = "manage_dept_id",property = "manageDeptId"),
            @Result(column = "last_login_date",property = "lastLoginDate"),
            @Result(column = "manage_dept_id",property = "manageDeptId"),
            @Result(column = "create_by",property = "createBy"),
            @Result(column = "update_by",property = "updateBy"),
            @Result(column = "building_code",property = "buildingCode"),
            @Result(column = "del_flag",property = "delFlag"),
            @Result(column = "sort",property = "sort"),
            @Result(column = "is_student",property = "isStudent"),
            @Result(column = "create_date",property = "createDate"),
            @Result(column = "update_date",property = "updateDate"),
            @Result(column = "remarks",property = "remarks")
    })
    List<User> findAll();

    @Select("SELECT b.* FROM sys_user_dept as a,sys_user as b where a.user_id=b.id and a.is_student=#{is_student} and a.dept_id=#{deptId};")
    @ResultMap("userMap")
    List<User> findMapperByUsersByDeptId(@Param("deptId") int deptId, @Param("is_student") int isStudent);
    @Select("select user_name,photo from sys_user where photo_flag=#{photoFlag}")
    @ResultMap("userMap")
    List<User> findMapperByPhotoFlag(@Param("photoFlag")Integer photoFlag);



    //通过宿舍楼code统计这个宿舍楼下的所有学生数
    @Select(value = "<script>" +
            "select count(1) from sys_user as a where " +
            "cast((SELECT model.roleId FROM sys_user_role model WHERE model.userId = a.id) AS CHAR) =3" +
            "<if test='buildingCodes!=null'> and <foreach item='item'  index='index' collection='buildingCodes' open='(' close=')' separator='or'>a.building_code like concat(#{item},'%')</foreach></if>" +
            "</script>")
    long countLikeByBuildingCodes(@Param("buildingCodes")List<String> buildingCodes);
}
