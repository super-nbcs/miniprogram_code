package com.zfw.core.sys.controller;

import com.alibaba.fastjson.JSONObject;
import com.zfw.core.annotation.LogAnnotation;
import com.zfw.core.annotation.Validate;
import com.zfw.core.exception.GlobalException;
import com.zfw.core.sys.entity.Duty;
import com.zfw.core.sys.service.IDutyService;
import com.zfw.utils.JpaFilterUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Api(tags = "职位管理相关接口")
@Controller
public class DutyController extends BaseController {

    @Autowired
    private IDutyService dutyService;

    @Autowired
    private HttpServletRequest request;

    @ApiOperation("获取所有职位信息")
    @GetMapping("/duties")
    public JSONObject findAllDuties() {
        return success(dutyService.findAll(JpaFilterUtils.dynamicSpecifications(request, Duty.class), dynamicAnalysisRequest(request)));
    }

    @ApiOperation("根据id获取职位信息")
    @GetMapping("/duty/{id}")
    public JSONObject findDutyById(@PathVariable("id") int id) {
        if (id < 1) {
            throw new GlobalException("职位id不正确");
        }
        return success(dutyService.findDutyById(id));
    }

    @LogAnnotation("创建职位")
    @ApiOperation("创建职位")
    @PostMapping("/duty")
    @Validate
    public JSONObject addDuty(Duty duty) {
        return success(dutyService.addDuty(duty));
    }

    @LogAnnotation("修改职位")
    @ApiOperation("修改职位")
    @PutMapping("/duty")
    @Validate
    public JSONObject updateDuty(Duty duty) {
        if (duty.getId() == null || duty.getId() < 1) {
            return fail("职位id不能为空");
        }
        if (!dutyService.existsById(duty.getId())) {
            return fail("职位不存在，无法修改");
        }
        return success(dutyService.updateDuty(duty));
    }

    @LogAnnotation("删除职位")
    @ApiOperation("删除职位")
    @DeleteMapping("/duty/{id}")
    public JSONObject deleteDuty(@PathVariable("id") int id) {
        if (!dutyService.existsById(id) || id < 1) {
            return fail("职位不存在，无法修改");
        }
        dutyService.deleteDuty(id);
        return success();
    }

}
