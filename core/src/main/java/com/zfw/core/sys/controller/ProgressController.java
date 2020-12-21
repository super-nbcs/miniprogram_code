package com.zfw.core.sys.controller;

import com.alibaba.fastjson.JSONObject;
import com.zfw.core.sys.entity.Progress;
import com.zfw.core.sys.service.IProgressService;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @Author:yzh
 * @Date:2019/12/16
 * @Content:    进度条
 */
@Api(tags = "进度条")
@Controller
public class ProgressController extends BaseController {
    protected Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private IProgressService iProgressService;
    @GetMapping("progress/{progressFlag}")
    public JSONObject getProgressFlag(@PathVariable(value = "progressFlag") String progressFlag){
        Progress progress = iProgressService.findTopByProgressFlag(progressFlag);
        return success(progress);
    }
}
