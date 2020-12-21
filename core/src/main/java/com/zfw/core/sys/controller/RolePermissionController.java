package com.zfw.core.sys.controller;

import com.alibaba.fastjson.JSONObject;
import com.zfw.core.annotation.LogAnnotation;
import com.zfw.core.exception.GlobalException;
import com.zfw.core.sys.service.IRolePermissionService;
import com.zfw.core.sys.service.IRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;


@Api(tags = "角色-数据权限相关接口")
@Controller
public class RolePermissionController extends BaseController {

    @Autowired
    private IRolePermissionService rolePermissionService;

    @Autowired
    private IRoleService roleService;

    @LogAnnotation("角色绑定数据权限")
    @ApiOperation(value = "角色绑定数据权限")
    @PostMapping("/permission/bind")
    public JSONObject roleBindPermission(Integer flag, Integer roleId, String deptIds) {
        return success(rolePermissionService.bindRolePermission(flag, roleId, deptIds));
    }

    @ApiOperation(value = "通过角色id查找绑定的数据权限", notes = "id不能为null")
    @GetMapping("/permission/roleId/{roleId}")
    public JSONObject getPermissionByRoleId(@PathVariable("roleId") Integer roleId) {
        if (!roleService.existsById(roleId)) {
            throw new GlobalException("角色不存在");
        }
        return success(rolePermissionService.getPermissionByRoleId(roleId));
    }

}
