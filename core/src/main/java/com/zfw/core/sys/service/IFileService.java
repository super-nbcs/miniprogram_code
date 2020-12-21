package com.zfw.core.sys.service;

import com.alibaba.fastjson.JSONObject;
import com.zfw.utils.FileStore.FileStoreUtils;
import com.zfw.utils.nuc32.FaceNuc32HttpUtils;
import com.zfw.utils.nuc32.NucInterface;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.HashMap;

/**
 * @Author:zfw
 * @Date:2020-11-23
 * @Content:
 */
@Component
public class IFileService {

    @Value("${photo.addr}")
    private String addr;
    @Value("${photo.userName}")
    private String userName;
    @Value("${photo.password}")
    private String password;
    /**
     * 检测图片质量
     * @param file
     * @return
     */
    public JSONObject check(String file) {
        String url = String.format("%s/%s", addr, NucInterface.KOALA_2_9.PATH);
        HashMap<String, File> fileMap = new HashMap<>();
        fileMap.put("photo", new File(FileStoreUtils.systemParentPath()+file));
        JSONObject post = FaceNuc32HttpUtils.post(url, null, fileMap, userName, password);
        return post;
    }

    public JSONObject check(File file) {
        HashMap<String, File> fileMap = new HashMap<>();
        fileMap.put("photo", file);

        String url = String.format("%s/%s", addr, NucInterface.KOALA_2_9.PATH);
        JSONObject post = FaceNuc32HttpUtils.post(url, null, fileMap, userName, password);
        return post;
    }
}
