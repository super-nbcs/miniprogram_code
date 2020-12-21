package com.zfw.core.sys.controller;

import com.alibaba.fastjson.JSONObject;
import com.zfw.core.annotation.Validate;
import com.zfw.core.exception.GlobalException;
import com.zfw.core.sys.entity.Menu;
import com.zfw.core.sys.service.IMenuService;
import com.zfw.core.sys.service.IRoleMenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Set;

import static com.zfw.core.constant.Constant.NOT_FOUND_ID;
import static com.zfw.core.constant.Constant.NOT_NULL_ID;

/**
 * @Author:zfw
 * @Date:2019/7/15
 * @Content:
 */

@Api(tags = "用户菜单相关接口")
@Controller
public class MenuController extends BaseController {
    @Autowired
    private IMenuService iMenuService;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private IRoleMenuService iRoleMenuService;

    @ApiOperation(value = "获取所有菜单")
    @GetMapping("menus")
    public JSONObject menus(Menu menu){
        return success(iMenuService.findAll(menu));
    }

    @ApiOperation(value = "获取所有菜单,并以树形结构返回")
    @GetMapping("menus/tree")
    public JSONObject treeMenus(){
        return success(iMenuService.getAllTreeMenus());
    }

    @ApiOperation(value = "创建菜单", notes = "根据业务需求创建菜单名")
    @Validate
    @PostMapping("menu")
    public JSONObject createMenu(Menu menu) {
        Menu save = iMenuService.save(menu);
        //每创建一个菜单，都为admin赋予此菜单权限
        iRoleMenuService.bindMenu(1,save.getId());
        return success(save);
    }

    @ApiOperation(value = "通过id查找菜单", notes = "id不能为null")
    //@ApiImplicitParam(name = "id", value = "菜单id", required = true,dataType = "Long")
    @GetMapping("menu/{id}")
    public JSONObject getMenuById(@PathVariable("id") Integer id) {
        if (iMenuService.existsById(id)) {
            Menu menu = iMenuService.getById(id);
            Set<Menu> menus = iMenuService.findByParentId(menu.getId());
            menu.setChildren(new ArrayList<>(menus));
            return success(menu);
        } else {
            throw new GlobalException(NOT_FOUND_ID.CODE, NOT_FOUND_ID.ZH_CODE + id, NOT_FOUND_ID.EN_CODE + id);
        }
    }

    @ApiOperation(value = "通过id删除菜单项", notes = "id不能为null")
    @DeleteMapping("menu/{id}")
    public JSONObject deleteMenuById(@PathVariable("id") Integer id){
        if (iMenuService.existsById(id)) {
            iMenuService.deleteMenu(id);
            return success();
        } else {
            throw new GlobalException(NOT_FOUND_ID.CODE, NOT_FOUND_ID.ZH_CODE + id, NOT_FOUND_ID.EN_CODE + id);
        }
    }


    @ApiOperation(value = "修改菜单", notes = "通过id修改menu内容,id必填")
    @Validate
    @PutMapping("menu")
    public JSONObject updateMenu(Menu menu) {
        if (menu.getId()==null){
            throw new GlobalException(NOT_NULL_ID);
        }else {
            menu=iMenuService.save(menu);
            return success(menu);
        }
    }

    @ApiOperation(value = "获取所有一级菜单")
    @GetMapping("menus/root")
    public JSONObject getRootMenu(){
        return success(iMenuService.findByParentId(0));
    }
}
