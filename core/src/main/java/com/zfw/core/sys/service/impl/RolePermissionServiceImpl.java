package com.zfw.core.sys.service.impl;

import com.zfw.core.constant.Constant;
import com.zfw.core.exception.GlobalException;
import com.zfw.core.service.impl.CommonServiceImpl;
import com.zfw.core.sys.dao.IRoleDao;
import com.zfw.core.sys.dao.IRolePermissionDao;
import com.zfw.core.sys.entity.RolePermission;
import com.zfw.core.sys.service.IRolePermissionService;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class RolePermissionServiceImpl extends CommonServiceImpl<RolePermission, Integer> implements IRolePermissionService {

    @Autowired
    private IRolePermissionDao rolePermissionDao;

    @Autowired
    private IRoleDao roleDao;

    @Override
    public RolePermission bindRolePermission(int flag, int roleId, String deptIds) {
        RolePermission rolePermission = null;
        switch (flag) {
            case 0:
//                本人权限
                rolePermission = createRolePermission(roleId, "00000000");
                break;
            case 1:
//                全部权限
                rolePermission = createRolePermission(roleId, "11111111");
                break;
            case 2:
//                自定义权限
                rolePermission = createRolePermission(roleId, deptIds);
                break;
            default:
                throw new GlobalException("角色绑定权限标记不正确");
        }
        return rolePermission;
    }

    @Override
    public RolePermission createRolePermission(int roleId, String deptIds) {
        if (roleId < 1 || !roleDao.existsById(roleId)) {
            throw new GlobalException(Constant.CODE_20014);
        }
        RolePermission rp = rolePermissionDao.findByRoleId(roleId);
        if (ObjectUtils.isNotEmpty(rp)) {
            rp.setDeptIds(deptIds);
        } else {
            rp = new RolePermission();
            rp.setRoleId(roleId).setDeptIds(deptIds);
        }
        return this.save(rp);
    }

    @Override
    public void deleteByRoleId(Integer roleId) {
        rolePermissionDao.deleteByRoleId(roleId);
    }

    @Override
    public Map<String, Object> getPermissionByRoleId(Integer roleId) {
        RolePermission rp = this.findByRoleId(roleId);
        if (ObjectUtils.isEmpty(rp)) {
            throw new GlobalException("该角色尚未绑定任何数据权限");
        }
        String permissionType = "userDefined";
        if (StringUtils.equals(rp.getDeptIds(), "00000000")) {
            permissionType = "personal";
        }
        if (StringUtils.equals(rp.getDeptIds(), "11111111")) {
            permissionType = "all";
        }
        String[] deptIdList = rp.getDeptIds().split(",");
        Map<String, Object> map = new HashMap<>();
        map.put("permissionType", permissionType);
        map.put("deptIds", deptIdList);
        return map;
    }

    @Override
    public RolePermission findByRoleId(Integer roleId) {
        return rolePermissionDao.findByRoleId(roleId);
    }
}
