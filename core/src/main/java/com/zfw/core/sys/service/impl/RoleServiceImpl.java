package com.zfw.core.sys.service.impl;


import com.zfw.core.exception.GlobalException;
import com.zfw.core.service.impl.CommonServiceImpl;
import com.zfw.core.sys.dao.IRoleDao;
import com.zfw.core.sys.entity.Menu;
import com.zfw.core.sys.entity.Role;
import com.zfw.core.sys.entity.RoleMenu;
import com.zfw.core.sys.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.zfw.core.constant.Constant.NOT_FOUND_ID;

;

@Service
public class RoleServiceImpl extends CommonServiceImpl<Role,Integer> implements IRoleService {
    @Autowired
    private IRoleMenuService iRoleMenuService;

    @Autowired
    private IRoleButtonService iRoleButtonService;

    @Autowired
    private IMenuService iMenuService;
    @Autowired
    private IRoleDao iRoleDao;

    @Autowired
    private IUserRoleService iUserRoleService;

    @Override
    public Role save(Role role) {
        return super.save(role);
    }

    @Override
    public List<Role> findAll() {
        List<Role> roles = super.findAll();
        roles.forEach(role -> {
        });
        return roles;
    }

    @Override
    public Role getRoleMenus(Integer roleId) {
        if (!this.existsById(roleId)){
            throw new GlobalException(NOT_FOUND_ID);
        }
        Role role = this.getById(roleId);
        Set<RoleMenu> roleMenus = iRoleMenuService.findByRoleId(roleId);
        Set<Menu> menus=new HashSet<>();
        roleMenus.forEach(roleMenu -> {
            Integer menuId = roleMenu.getMenuId();
            Menu menu = iMenuService.getById(menuId);
            menus.add(menu);
        });
        role.setMenus(menus);
        return role;
    }

    @Override
    public boolean existsByName(String name) {
        return iRoleDao.existsByName(name);
    }

    @Override
    public boolean existsByCode(String code) {
        return iRoleDao.existsByCode(code);
    }

    @Override
    @Transactional
    public void deleteRole(Integer id) {
        this.deleteById(id);
        iUserRoleService.deleteAllByRoleId(id);
        iRoleMenuService.deleteAllByRoleId(id);
        iRoleButtonService.deleteAllByRoleId(id);
    }

    @Override
    public Role findByCode(String code) {
        return iRoleDao.findByCode(code);
    }

    @Override
    public Role findByName(String name) {
        return iRoleDao.findByName(name);
    }
}

