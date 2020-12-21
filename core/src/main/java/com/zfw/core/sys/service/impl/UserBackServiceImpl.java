package com.zfw.core.sys.service.impl;
import com.zfw.core.exception.GlobalException;
import com.zfw.core.sys.entity.User;
import com.zfw.core.sys.entity.UserBack;
import com.zfw.core.sys.dao.IUserBackDao;
import com.zfw.core.sys.service.IUserBackService;
import com.zfw.core.service.impl.CommonServiceImpl;
import java.util.List;

import com.zfw.core.sys.service.IUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.lang.String;
import java.lang.Integer;


/**
* @Author:zfw
* @Date:2020-10-14
* @Content: 退宿记录service实现类接口
*/
@Service
public class UserBackServiceImpl extends CommonServiceImpl<UserBack,Integer> implements IUserBackService{

    @Autowired
    private IUserBackDao iUserBackDao;
    @Autowired
    private IUserService iUserService;


    @Override
    public List<UserBack> findByGender(Integer gender){return iUserBackDao.findByGender(gender);}
    @Override
    public boolean existsByGender(Integer gender){return iUserBackDao.existsByGender(gender);}

    @Override
    public List<UserBack> findByIdCard(String idCard){return iUserBackDao.findByIdCard(idCard);}
    @Override
    public boolean existsByIdCard(String idCard){return iUserBackDao.existsByIdCard(idCard);}

    @Override
    public List<UserBack> findByDeptId(Integer deptId){return iUserBackDao.findByDeptId(deptId);}
    @Override
    public boolean existsByDeptId(Integer deptId){return iUserBackDao.existsByDeptId(deptId);}

    @Override
    public List<UserBack> findByBuildingCode(String buildingCode){return iUserBackDao.findByBuildingCode(buildingCode);}
    @Override
    public boolean existsByBuildingCode(String buildingCode){return iUserBackDao.existsByBuildingCode(buildingCode);}

    @Override
    public List<UserBack> findByPhoto(String photo){return iUserBackDao.findByPhoto(photo);}
    @Override
    public boolean existsByPhoto(String photo){return iUserBackDao.existsByPhoto(photo);}

    @Override
    public List<UserBack> findByLastLoginDate(Date lastLoginDate){return iUserBackDao.findByLastLoginDate(lastLoginDate);}
    @Override
    public boolean existsByLastLoginDate(Date lastLoginDate){return iUserBackDao.existsByLastLoginDate(lastLoginDate);}

    @Override
    public List<UserBack> findByUserName(String userName){return iUserBackDao.findByUserName(userName);}
    @Override
    public boolean existsByUserName(String userName){return iUserBackDao.existsByUserName(userName);}

    @Override
    public List<UserBack> findByDutyName(String dutyName){return iUserBackDao.findByDutyName(dutyName);}
    @Override
    public boolean existsByDutyName(String dutyName){return iUserBackDao.existsByDutyName(dutyName);}

    @Override
    public List<UserBack> findByCanLogin(Integer canLogin){return iUserBackDao.findByCanLogin(canLogin);}
    @Override
    public boolean existsByCanLogin(Integer canLogin){return iUserBackDao.existsByCanLogin(canLogin);}

    @Override
    public List<UserBack> findByPhone(String phone){return iUserBackDao.findByPhone(phone);}
    @Override
    public boolean existsByPhone(String phone){return iUserBackDao.existsByPhone(phone);}

    @Override
    public List<UserBack> findByBuildingPath(String buildingPath){return iUserBackDao.findByBuildingPath(buildingPath);}
    @Override
    public boolean existsByBuildingPath(String buildingPath){return iUserBackDao.existsByBuildingPath(buildingPath);}

    @Override
    public List<UserBack> findByGrade(String grade){return iUserBackDao.findByGrade(grade);}
    @Override
    public boolean existsByGrade(String grade){return iUserBackDao.existsByGrade(grade);}

    @Override
    public List<UserBack> findByName(String name){return iUserBackDao.findByName(name);}
    @Override
    public boolean existsByName(String name){return iUserBackDao.existsByName(name);}

    @Override
    public List<UserBack> findByDeptPath(String deptPath){return iUserBackDao.findByDeptPath(deptPath);}
    @Override
    public boolean existsByDeptPath(String deptPath){return iUserBackDao.existsByDeptPath(deptPath);}

    @Override
    public List<UserBack> findByEmail(String email){return iUserBackDao.findByEmail(email);}
    @Override
    public boolean existsByEmail(String email){return iUserBackDao.existsByEmail(email);}

    @Override
    public List<UserBack> findByDeptCode(String deptCode){return iUserBackDao.findByDeptCode(deptCode);}
    @Override
    public boolean existsByDeptCode(String deptCode){return iUserBackDao.existsByDeptCode(deptCode);}

    @Override
    @Transactional
    public void returnSchool(Integer id) {
        if (!existsById(id)){
            throw new GlobalException("系统找不到此条记录，可能被其它用户删除了");
        }
        UserBack userBack = getById(id);
        User user = new User();
        BeanUtils.copyProperties(userBack, user);
        user.setId(null).setRemarks("这个学生是从退宿表中复学回来的");
        iUserService.createUser(user);
        deleteById(id);
    }
}

