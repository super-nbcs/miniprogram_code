package com.zfw.core.sys.controller;


import com.alibaba.fastjson.JSONObject;
import com.zfw.core.annotation.UnAuthorized;
import com.zfw.core.annotation.Validate;
import com.zfw.core.constant.Constant;
import com.zfw.core.entity.Pager;
import com.zfw.core.exception.GlobalException;
import com.zfw.core.sys.entity.UserBack;
import com.zfw.core.sys.service.IUserBackService;
import com.zfw.utils.JpaFilterUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

/**
* @Author: zfw
* @Date: 2020-10-14
* @Content: 退宿记录控制器
*/
@Api(tags = "退宿记录相关接口")
@Controller
public class UserBackController extends BaseController {
    @Autowired
    private IUserBackService iUserBackService;
    @Autowired
    private HttpServletRequest request;

    @ApiOperation(value = "退宿记录列表", notes = "返回退宿记录列表信息")
    @GetMapping("userBacks")
    public JSONObject userBacks(){
        Pager<UserBack> all = iUserBackService.findAll(JpaFilterUtils.dynamicSpecificationManyValues(request, new HashMap<>(), UserBack.class), dynamicAnalysisRequest(request));
        return success(all);
    }

    @ApiOperation(value = "通过id获取单个退宿记录", notes = "返回单个退宿记录信息")
    @GetMapping("userBack/{id}")
    @Validate
    public JSONObject getUserBack(@PathVariable("id") Integer id){
        if(id==null){
            throw new GlobalException(Constant.NOT_NULL_ID);
        }
        return success(iUserBackService.getById(id));
    }

    @ApiOperation(value = "创建退宿记录", notes = "返回创建退宿记录信息")
    @PostMapping("userBack")
    @Validate
    public JSONObject createUserBack(UserBack userBack){
        return success(iUserBackService.save(userBack));
    }



    @ApiOperation(value = "修改退宿记录", notes = "返回修改退宿记录信息")
    @PutMapping("userBack")
    public JSONObject updateUserBack(UserBack userBack){
        if(userBack.getId()==null){
            throw new GlobalException(Constant.NOT_NULL_ID);
        }
        return success(iUserBackService.save(userBack));
    }


    @ApiOperation(value = "通过id删除退宿记录", notes = "删除退宿记录信息")
    @DeleteMapping("userBack/{id}")
    public JSONObject deleteUserBack(@PathVariable("id") Integer id){
        if(id==null){
            throw new GlobalException(Constant.NOT_NULL_ID);
        }
        iUserBackService.deleteById(id);
        return success();
    }

    @UnAuthorized
    @ApiOperation(value = "复学接口")
    @PostMapping("userBack/return/{id}")
    public JSONObject returnUser(@PathVariable("id") Integer id){
        iUserBackService.returnSchool(id);
        return success();
    }

}
