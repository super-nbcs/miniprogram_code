package com.zfw.core.sys.controller;

import com.alibaba.fastjson.JSONObject;
import com.zfw.core.annotation.UnAuthorized;
import com.zfw.core.sys.service.IFileService;
import com.zfw.utils.FileStore.FileStoreUtils;
import com.zfw.utils.FileStore.ImageUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @Author:zfw
 * @Date:2019/8/31
 * @Content:
 */
@Api(tags = "文件上传接口,不需要鉴权")
@Controller
public class FileController extends BaseController {

    @Autowired
    private IFileService iFileService;
    @UnAuthorized
    @ApiOperation("仅上传图片")
    @PostMapping("file/upload")
    public JSONObject uploadFile(MultipartFile file) throws IOException {
        boolean photo = FileStoreUtils.isPhoto(file);
        if (!photo) {
            return fail("请上传图片文件");
        }
        String filePath = FileStoreUtils.createFile(file);
        ImageUtils.compress(filePath);
        return success(filePath);
    }


    @UnAuthorized
    @ApiOperation("上传图片，并进行质量检测")
    @PostMapping("file/upload/check")
    public JSONObject uploadImg(MultipartFile file) throws IOException {
        if (!FileStoreUtils.isPhoto(file)) {
            return fail("请上传图片文件");
        }
        String filePath = FileStoreUtils.createFile(file);
        ImageUtils.compress(filePath);
        JSONObject check = iFileService.check(filePath);
        if (check.getInteger("code")==0){
            return success(filePath);
        }
        FileStoreUtils.deleteFile(filePath);
        return fail(check.getString("desc"));
    }
    @ApiOperation("根据上传返回路径，删除上传的文件")
    @DeleteMapping("file/delete/{filePath}")
    public JSONObject deleteFilePath(@PathVariable("filePath")String filePath){
        FileStoreUtils.deleteFile(filePath);
        return success();
    }







}
