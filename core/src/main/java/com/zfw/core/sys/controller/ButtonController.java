package com.zfw.core.sys.controller;


import com.alibaba.fastjson.JSONObject;
import com.zfw.core.annotation.Validate;
import com.zfw.core.constant.Constant;
import com.zfw.core.exception.GlobalException;
import com.zfw.core.sys.entity.Button;
import com.zfw.core.sys.entity.Menu;
import com.zfw.core.sys.service.IButtonService;
import com.zfw.core.sys.service.IMenuService;
import com.zfw.core.sys.service.IRoleButtonService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author:zfw
 * @Date:2020/2/7
 * @Content:
 */
@Api(tags = "按钮权限接口")
@Controller
public class ButtonController extends BaseController {
    @Autowired
    private IButtonService iButtonService;
    @Autowired
    private IRoleButtonService iRoleButtonService;
    @Autowired
    private IMenuService iMenuService;

    @ApiOperation(value = "创建按钮")
    @PostMapping("button")
    @Validate
    public JSONObject createDict(Button button){
        if(iButtonService.existsByBtnSign(button.getBtnSign())){
            throw new GlobalException("按钮唯一标识已存在");
        }
        //每创建一个按钮，都为admin赋予此菜单权限
        Button save = iButtonService.save(button);
        iRoleButtonService.bindBtn(1,save.getId());
        return success(save);
    }
    @ApiOperation(value = "获取所有按钮")
    @GetMapping("buttons")
    public JSONObject getButtons(Button button) {
        return success(iButtonService.findAll(button));
    }
    @ApiOperation(value = "删除按钮")
    @DeleteMapping("button/{id}")
    public JSONObject deleteButton(@PathVariable("id") Integer id){
        if(id==null){
            throw new GlobalException(Constant.NOT_NULL_ID);
        }
        if(iButtonService.getById(id)==null){
            throw new GlobalException(Constant.CODE_10018);
        }
        iButtonService.deleteBtn(id);
        return success();
    }

    @ApiOperation(value = "获取所有按钮,返回checkBox所需的数据结构")
    @GetMapping("/buttons/checkbox")
    public JSONObject getButtonsCheckbox(){
        List<Button> buttons = iButtonService.findAll();
        Map<Integer, List<Button>> buttonMap = buttons.stream().collect(Collectors.groupingBy(Button::getMenuId));
        Map<String, List<Button>> buttonMaps=new HashMap<>();
        List<ButtonCheckBox> buttonCheckBoxs=new ArrayList<>();
        buttonMap.forEach((k,v)->{
            Menu menu = iMenuService.getById(k);
            if (menu.isRoot()){
                ButtonCheckBox buttonCheckBox = new ButtonCheckBox();
                buttonCheckBox.name=menu.getName();
                buttonCheckBox.menuId=menu.getId();
                buttonCheckBox.buttons=v;
                buttonCheckBoxs.add(buttonCheckBox);
            }else {
                Menu parentMenu = iMenuService.getById(menu.getParentId());

                ButtonCheckBox buttonCheckBox = new ButtonCheckBox();
                buttonCheckBox.name=parentMenu.getName()+"/"+menu.getName();
                buttonCheckBox.menuId=menu.getId();
                buttonCheckBox.buttons=v;
                buttonCheckBoxs.add(buttonCheckBox);

            }
        });
        return success(buttonCheckBoxs);
    }
    class ButtonCheckBox{
        public String name;
        public int menuId;
        public Boolean isIndeterminate=false;
        public List<Button> buttons;
    }
}
