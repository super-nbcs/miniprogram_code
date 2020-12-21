package com.zfw.core.sys.dao;

import com.zfw.core.dao.ICommonDao;
import com.zfw.core.sys.entity.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface IUserDao extends ICommonDao<User, Integer> {
    boolean existsByUserName(String userName);

    User getById(Integer id);

    User findByUserName(String userName);

    User findTop1ByMiniOpenId(String miniOpenId);

    /**
     * 判断手机号是否已注册
     *
     * @param phone
     * @return
     */
    boolean existsByPhone(String phone);

    /**
     * 根据手机号查找用户
     *
     * @param phone
     * @return
     */
    User findByPhone(String phone);

    /**
     * 根据部门id判断数据是否存在
     *
     * @param deptId
     * @return
     */
    boolean existsByDeptId(Integer deptId);

    /**
     * 查找所有未上传照片员工信息
     *
     * @return
     */
    List<User> findAllByPhotoNull();

    List<User> findByDeptCodeIsStartingWith(String deptCode);

    List<User> findByPhotoIsNotNull();

    List<User> findByPhotoFlag(Integer photoFlag);

    @Modifying
    @Query(value = "update sys_user set dept_id=:deptId,dept_path=:deptPath,dept_code=:deptCode where user_name=:userName", nativeQuery = true)
    @Transactional
    void updateDeptCode(@Param("userName") String userName, @Param("deptId") Integer deptId, @Param("deptPath") String deptPath, @Param("deptCode") String deptCode);
}
