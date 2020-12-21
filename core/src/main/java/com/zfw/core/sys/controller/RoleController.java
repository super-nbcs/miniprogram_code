package com.zfw.core.sys.controller;

import com.alibaba.fastjson.JSONObject;
import com.zfw.core.annotation.Validate;
import com.zfw.core.exception.GlobalException;
import com.zfw.core.sys.entity.Button;
import com.zfw.core.sys.entity.Role;
import com.zfw.core.sys.service.IRoleButtonService;
import com.zfw.core.sys.service.IRoleMenuService;
import com.zfw.core.sys.service.IRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.zfw.core.constant.Constant.*;

/**
 * @Author:zfw
 * @Date:2019/7/29
 * @Content:
 */
@Api(tags = "用户角色相关接口")
@Controller
public class RoleController extends BaseController {
    @Autowired
    private IRoleService iRoleService;

    @Autowired
    private IRoleMenuService iRoleMenuService;

    @Autowired
    private IRoleButtonService iRoleButtonService;
    @ApiOperation(value = "通过id查找角色", notes = "id不能为null")
    @GetMapping("/role/{id}")
    public JSONObject getRoleById(@PathVariable("id") Integer id) {
        if (iRoleService.existsById(id)) {
            Role role = iRoleService.getRoleMenus(id);
            return success(role);
        } else {
            throw new GlobalException(NOT_FOUND_ID.CODE, NOT_FOUND_ID.ZH_CODE + id, NOT_FOUND_ID.EN_CODE + id);
        }
    }
    @ApiOperation(value = "获取所有角色名")
    @GetMapping("roles")
    public JSONObject getRoles(Role role) {
        List<Role> all = iRoleService.findAll(role);
        return success(all);
    }

    @Validate
    @ApiOperation(value = "添加角色")
    @PostMapping("role")
    public JSONObject addRole(Role role){
        if (iRoleService.existsByName(role.getName())){
            throw new GlobalException(CODE_20011);
        }
        if (iRoleService.existsByCode(role.getCode())){
            throw new GlobalException(CODE_20012);
        }
        Role save= iRoleService.save(role);
        return success(save);
    }

    @Validate
    @ApiOperation(value = "修改角色", notes = "通过id修改role内容,id必填")
    @PutMapping("role")
    public JSONObject updateRole(Role role) {
        if (role.getId()==null){
            throw new GlobalException(NOT_NULL_ID);
        }else {
            role=iRoleService.save(role);
            return success(role);
        }
    }

    @ApiOperation(value = "通过id删除角色", notes = "id不能为null")
    @DeleteMapping("role/{id}")
    public JSONObject deleteRoleById(@PathVariable("id") Integer id){
        if (iRoleService.existsById(id)) {
            iRoleService.deleteRole(id);
            return success();
        } else {
            throw new GlobalException(NOT_FOUND_ID.CODE, NOT_FOUND_ID.ZH_CODE + id, NOT_FOUND_ID.EN_CODE + id);
        }
    }
    @ApiOperation(value = "角色和菜单绑定")
    @PostMapping("role/bind")
    public JSONObject bindMenu(Integer roleId,@RequestParam("menuIds") List<Integer> menuIds){
        iRoleMenuService.bindMenu(roleId,menuIds);
        return success();
    }
    @ApiOperation(value = "角色和按钮绑定")
    @PostMapping("role/bind/btns")
    public JSONObject bindBtn(Integer roleId,@RequestParam("btnIds") List<Integer> btnIds){
        iRoleButtonService.bindBtn(roleId,btnIds);
        return success();
    }

    @ApiOperation(value = "获取当前角色下所有按钮")
    @GetMapping("role/btns/{roleId}")
    public JSONObject roleBtns(@PathVariable(value = "roleId") int roleId){
        List<Button> buttons = iRoleButtonService.findBtnsByRoleId(roleId);
        return success(buttons);
    }


}
