package com.zfw.core.sys.dao;
import com.zfw.core.dao.ICommonDao;
import com.zfw.core.sys.entity.UserBack;
import java.util.List;
import java.util.Date;
import java.lang.String;
import java.lang.Integer;

/**
* @Author: zfw
* @Date: 2020-10-14
* @Content: 退宿记录
*/
public interface IUserBackDao extends ICommonDao<UserBack,Integer> {

    List<UserBack> findByGender(Integer gender);

    boolean existsByGender(Integer gender);

    List<UserBack> findByIdCard(String idCard);

    boolean existsByIdCard(String idCard);

    List<UserBack> findByDeptId(Integer deptId);

    boolean existsByDeptId(Integer deptId);

    List<UserBack> findByBuildingCode(String buildingCode);

    boolean existsByBuildingCode(String buildingCode);

    List<UserBack> findByPhoto(String photo);

    boolean existsByPhoto(String photo);

    List<UserBack> findByLastLoginDate(Date lastLoginDate);

    boolean existsByLastLoginDate(Date lastLoginDate);

    List<UserBack> findByUserName(String userName);

    boolean existsByUserName(String userName);

    List<UserBack> findByDutyName(String dutyName);

    boolean existsByDutyName(String dutyName);

    List<UserBack> findByCanLogin(Integer canLogin);

    boolean existsByCanLogin(Integer canLogin);

    List<UserBack> findByPhone(String phone);

    boolean existsByPhone(String phone);

    List<UserBack> findByBuildingPath(String buildingPath);

    boolean existsByBuildingPath(String buildingPath);

    List<UserBack> findByGrade(String grade);

    boolean existsByGrade(String grade);

    List<UserBack> findByName(String name);

    boolean existsByName(String name);

    List<UserBack> findByDeptPath(String deptPath);

    boolean existsByDeptPath(String deptPath);

    List<UserBack> findByEmail(String email);

    boolean existsByEmail(String email);

    List<UserBack> findByDeptCode(String deptCode);

    boolean existsByDeptCode(String deptCode);
}
