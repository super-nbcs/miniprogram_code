package com.zfw.core.sys.service.impl;

import com.zfw.core.exception.GlobalException;
import com.zfw.core.service.impl.BaseServiceImpl;
import com.zfw.core.sys.dao.IUserRoleDao;
import com.zfw.core.sys.entity.UserRole;
import com.zfw.core.sys.service.IRoleService;
import com.zfw.core.sys.service.IUserRoleService;
import com.zfw.core.sys.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

import static com.zfw.core.constant.Constant.NOT_FOUND_ID;

/**
 * @Author:zfw
 * @Date:2019/8/5
 * @Content:
 */
@Service
public class UserRoleServiceImpl extends BaseServiceImpl<UserRole,Integer> implements IUserRoleService {

    @Autowired
    private IUserService iUserService;
    @Autowired
    private IRoleService iRoleService;
    @Autowired
    private IUserRoleDao iUserRoleDao;

    @Override
    @Transactional(rollbackFor = GlobalException.class)
    public void bindRole(Integer userId, List<Integer> roleIds) {
        if (roleIds==null){
            iUserRoleDao.removeAllByUserId(userId);
        }else {
            iUserRoleDao.removeAllByUserId(userId);
            roleIds.forEach(roleId->this.bindRole(userId,roleId));
        }
    }

    @Override
    @Transactional(rollbackFor = GlobalException.class)
    public void bindRole(Integer userId, Integer roleId) {
        if (!iRoleService.existsById(roleId)||!iUserService.existsById(userId)){
            throw new GlobalException(NOT_FOUND_ID.CODE,NOT_FOUND_ID.ZH_CODE+roleId+"或"+userId,NOT_FOUND_ID.EN_CODE);
        }
        UserRole userRole= iUserRoleDao.findTopByUserIdAndRoleId(userId, roleId);
        if (userRole==null){
            userRole=new UserRole().setUserId(userId).setRoleId(roleId).setId(null);
        }
        this.save(userRole);
    }

    @Override
    public void deleteAllByUserId(Integer userId) {
        iUserRoleDao.removeAllByUserId(userId);
    }

    @Override
    public void deleteAllByRoleId(Integer roleId) {
        iUserRoleDao.removeAllByRoleId(roleId);
    }

    @Override
    public Set<UserRole> findByUserId(Integer userId) {
        return iUserRoleDao.findByUserId(userId);
    }
}
