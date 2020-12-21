package com.zfw.core.sys.service.impl;

import com.zfw.core.exception.GlobalException;
import com.zfw.core.service.impl.BaseServiceImpl;
import com.zfw.core.sys.dao.IRoleButtonDao;
import com.zfw.core.sys.entity.Button;
import com.zfw.core.sys.entity.RoleButton;
import com.zfw.core.sys.entity.RoleMenu;
import com.zfw.core.sys.service.IButtonService;
import com.zfw.core.sys.service.IRoleButtonService;
import com.zfw.core.sys.service.IRoleMenuService;
import com.zfw.core.sys.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.zfw.core.constant.Constant.NOT_FOUND_ID;

/**
 * @Author:zfw
 * @Date:2019/7/31
 * @Content:
 */
@Service
public class RoleButtonServiceImpl extends BaseServiceImpl<RoleButton,Integer> implements IRoleButtonService {
    @Autowired
    private IRoleButtonDao iRoleButtonDao;

    @Autowired
    private IButtonService iButtonService;

    @Autowired
    private IRoleService iRoleService;

    @Autowired
    private IRoleMenuService iRoleMenuService;
    @Override
    @Transactional(rollbackFor = GlobalException.class)
    public void bindBtn(Integer roleId, Integer btnId) {
        if (!iRoleService.existsById(roleId)||!iButtonService.existsById(btnId)){
            throw new GlobalException(NOT_FOUND_ID.CODE,NOT_FOUND_ID.ZH_CODE+roleId+"或"+btnId,NOT_FOUND_ID.EN_CODE);
        }
        RoleButton roleButton = iRoleButtonDao.findByRoleIdAndBtnId(roleId, btnId);
        if (roleButton==null){
            roleButton=new RoleButton().setRoleId(roleId).setBtnId(btnId);
        }
        this.save(roleButton);
    }

    @Override
    @Transactional(rollbackFor = GlobalException.class)
    public void bindBtn(Integer roleId, List<Integer> btnIds) {
        if (btnIds==null){
            iRoleButtonDao.removeAllByRoleId(roleId);
        }else {
            iRoleButtonDao.removeAllByRoleId(roleId);
            for (Integer btnId : btnIds) {
                Button button = iButtonService.getById(btnId);
                RoleMenu roleMenu = iRoleMenuService.findByRoleIdAndMenuId(roleId, button.getMenuId());
                if (roleMenu==null){
                    throw new GlobalException("按钮【"+button.getBtnName()+"】绑定失败,此角色没有绑定此按钮所在菜单");
                }
                this.bindBtn(roleId,btnId);
            }
        }
    }

    @Override
    public Set<RoleButton> findByRoleId(Integer roleId) {
        return iRoleButtonDao.findByRoleId(roleId);
    }

    @Override
    public void deleteAllByBtnId(Integer btnId) {
        iRoleButtonDao.removeAllByBtnId(btnId);
    }

    @Override
    public void deleteAllByRoleId(Integer roleId) {
        iRoleButtonDao.removeAllByRoleId(roleId);
    }

    @Override
    public List<Button> findBtnsByRoleId(int roleId) {
        Set<RoleButton> roleButtons = this.findByRoleId(roleId);
        List<Button> buttons=new ArrayList<>();
        for (RoleButton roleButton : roleButtons) {
            buttons.add(iButtonService.getById(roleButton.getBtnId()));
        }
        return buttons;
    }
}
