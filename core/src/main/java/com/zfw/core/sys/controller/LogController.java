package com.zfw.core.sys.controller;

import com.alibaba.fastjson.JSONObject;
import com.zfw.core.sys.entity.Log;
import com.zfw.core.sys.service.ILogService;
import com.zfw.utils.JpaFilterUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

@Api(tags = "操作日志相关接口")
@Controller
public class LogController extends BaseController {

    @Autowired
    private ILogService logService;

    @Autowired
    private HttpServletRequest request;

    @ApiOperation(value = "获取所有操作日志")
    @GetMapping("/logs")
    public JSONObject getLogs() {
        return success(logService.findAll(JpaFilterUtils.dynamicSpecifications(request, Log.class), dynamicAnalysisRequest(request)));
    }

}
