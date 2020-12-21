package com.zfw.core.sys.controller;

import com.alibaba.fastjson.JSONObject;
import com.zfw.core.annotation.UnAuthorized;
import com.zfw.core.constant.Constant;
import com.zfw.core.exception.GlobalException;
import com.zfw.core.sys.entity.ImportMsg;
import com.zfw.core.sys.service.IImportMsgService;
import com.zfw.utils.FileStore.FileStoreUtils;
import com.zfw.utils.JpaFilterUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * @Author:zfw
 * @Date:2019/10/11
 * @Content:
 */
@Api(tags = "导入文件时信息反馈")
@Controller
public class ImportMsgController extends BaseController {
    @Autowired
    private IImportMsgService iImportErrorMsgService;
    @Autowired
    private HttpServletRequest request;

    @ApiOperation(value = "获取所有导入失败表格", notes = "支持模糊、准确、分页查询")
    @GetMapping("/importMsgs")
    public JSONObject getImportMsgs(){
        return success(iImportErrorMsgService.findAll(JpaFilterUtils.dynamicSpecifications(request, ImportMsg.class),dynamicAnalysisRequest(request)));
    }

    @GetMapping("importErrorMsg/fileName/{fileName}")
    public JSONObject getImportErrorMsgByFileName(@PathVariable("fileName") String fileName){
        ImportMsg importErrorMsg = iImportErrorMsgService.findByFileName(fileName);
        if (importErrorMsg==null){
            return fail("no file");
        }else {
            return success();
        }
    }


    @ApiOperation(value = "统计未处理的数据")
    @GetMapping("importMsg/count")
    public JSONObject getImportCount(){
        long count=iImportErrorMsgService.countUntreated();
        return success(count);
    }

    @ApiOperation(value = "标记为已处理，通过id")
    @PostMapping("importMsg/Processed/{id}")
    public JSONObject processedImportMsg(@PathVariable("id") Integer id){
        ImportMsg importMsg = iImportErrorMsgService.getById(id);
        importMsg=importMsg.setState(Constant.IMPORT_PROCESSED);
        iImportErrorMsgService.save(importMsg);
        return success();
    }

    @ApiOperation(value = "标记为待处理，通过id")
    @PostMapping("importMsg/Untreated/{id}")
    public JSONObject untreatedImportMsg(@PathVariable("id") Integer id){
        ImportMsg importMsg = iImportErrorMsgService.getById(id);
        importMsg=importMsg.setState(Constant.IMPORT_UNTREATED);
        iImportErrorMsgService.save(importMsg);
        return success();
    }

    @ApiOperation(value = "通过id删除导入反馈信息，id必须存在")
    @DeleteMapping("/importMsg/{id}")
    public JSONObject removeImportMsgById(@PathVariable(value = "id") Integer id){
        if (iImportErrorMsgService.existsById(id)) {
            new Thread(()->{
                ImportMsg importMsg = iImportErrorMsgService.getById(id);
                FileStoreUtils.deleteFile(importMsg.getErrorFileName());
            }).start();
            iImportErrorMsgService.deleteById(id);
            return success();
        } else {
            throw new GlobalException("此记录已被删除");
        }
    }

    @UnAuthorized
    @ApiOperation(value = "下载文件名")
    @GetMapping("importMsg/download")
    public void downloadFile(HttpServletResponse response,String fileName) throws IOException {
        String name = FileStoreUtils.systemParentPath() + fileName;
        File file = new File(name);
        if (!file.exists()){
            throw new GlobalException("此文件不存在");
        }
        FileInputStream fis = new FileInputStream(name);
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-Disposition", "attachment; filename=" + java.net.URLEncoder.encode(fileName, "UTF-8"));


        ServletOutputStream out = response.getOutputStream();
        int len = 0;
        byte[] buffer = new byte[1024];
        while((len = fis.read(buffer)) != -1) {
            out.write(buffer, 0, len);
        }
        fis.close();
        out.close();
    }

}
