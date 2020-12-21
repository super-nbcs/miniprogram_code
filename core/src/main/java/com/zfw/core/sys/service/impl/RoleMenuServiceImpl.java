package com.zfw.core.sys.service.impl;

import com.zfw.core.exception.GlobalException;
import com.zfw.core.service.impl.BaseServiceImpl;
import com.zfw.core.sys.dao.IRoleMenuDao;
import com.zfw.core.sys.entity.Menu;
import com.zfw.core.sys.entity.RoleMenu;
import com.zfw.core.sys.service.IMenuService;
import com.zfw.core.sys.service.IRoleMenuService;
import com.zfw.core.sys.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.zfw.core.constant.Constant.NOT_FOUND_ID;

/**
 * @Author:zfw
 * @Date:2019/7/31
 * @Content:
 */
@Service
public class RoleMenuServiceImpl extends BaseServiceImpl<RoleMenu,Integer> implements IRoleMenuService {
    @Autowired
    private IRoleMenuDao iRoleMenuDao;

    @Autowired
    private IMenuService iMenuService;

    @Autowired
    private IRoleService iRoleService;
    @Override
    @Transactional(rollbackFor = GlobalException.class)
    public void bindMenu(Integer roleId, Integer menuId) {
        if (!iRoleService.existsById(roleId)||!iMenuService.existsById(menuId)){
            throw new GlobalException(NOT_FOUND_ID.CODE,NOT_FOUND_ID.ZH_CODE+roleId+"或"+menuId,NOT_FOUND_ID.EN_CODE);
        }
        RoleMenu roleMenu = iRoleMenuDao.findByRoleIdAndMenuId(roleId, menuId);
        if (roleMenu==null){
            roleMenu=new RoleMenu().setRoleId(roleId).setMenuId(menuId);
        }
        this.save(roleMenu);
    }

    @Override
    @Transactional(rollbackFor = GlobalException.class)
    public void bindMenu(Integer roleId, List<Integer> menuIds) {
        if (menuIds==null){
            iRoleMenuDao.removeAllByRoleId(roleId);
        }else {
            iRoleMenuDao.removeAllByRoleId(roleId);
            Set<Integer> menuSetIds=new HashSet<>();
            menuIds.forEach(menuId->{
                Menu menu = iMenuService.getById(menuId);
                Integer parentId = menu.getParentId();
                if (parentId!=0){
                    menuSetIds.add(parentId);
                }
                menuSetIds.add(menuId);
            });

            menuSetIds.forEach(item->{
                    this.bindMenu(roleId,item);
            });

        }
    }

    @Override
    public Set<RoleMenu> findByRoleId(Integer roleId) {
        return iRoleMenuDao.findByRoleId(roleId);
    }

    @Override
    public void deleteAllByMenuId(Integer menuId) {
        iRoleMenuDao.removeAllByMenuId(menuId);
    }

    @Override
    public void deleteAllByRoleId(Integer roleId) {
        iRoleMenuDao.removeAllByRoleId(roleId);
    }

    @Override
    public RoleMenu findByRoleIdAndMenuId(Integer roleId, Integer menuId) {
        return iRoleMenuDao.findByRoleIdAndMenuId(roleId,menuId);
    }
}
