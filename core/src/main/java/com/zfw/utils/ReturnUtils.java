package com.zfw.utils;

import com.alibaba.fastjson.JSONObject;
import com.zfw.core.constant.Constant;
import com.zfw.core.constant.ReturnCode;
import com.zfw.utils.FileStore.YamlConfigurerUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author:zfw
 * @Date:2019/7/11
 * @Content:
 */
public class ReturnUtils {
    public static Integer serverPort=YamlConfigurerUtils.getInteger("server.port","application");

    public static JSONObject success(Constant constant, Object date){
        return json(constant.CODE,constant.ZH_CODE,constant.EN_CODE,true,date);
    }
    public static JSONObject success(ReturnCode returnCode, Object data){
        return json(returnCode.getCode(),returnCode.getZhCode(),returnCode.getEnCode(),true,data);
    }

    public static JSONObject fail(Constant constant, Object data){
        return json(constant.CODE,constant.ZH_CODE,constant.EN_CODE,false,data);
    }
    public static JSONObject fail(ReturnCode returnCode, Object data){
        return json(returnCode.getCode(),returnCode.getZhCode(),returnCode.getEnCode(),false,data);
    }
    public static JSONObject fail(Object data){
        return json(Constant.FAIL.CODE,data.toString(),Constant.FAIL.EN_CODE,false,null);
    }
    public static JSONObject fail(Integer code,Object data){
        return json(code,"系统繁忙",data.toString(),false,data);
    }

    private static JSONObject json(Integer code,String zhCode,String enCode, boolean b, Object data) {
        Map<String,Object> map =new HashMap<>();
        map.put("code",code);
        map.put("zhCode",zhCode);
        map.put("enCode",enCode);
        map.put("data",data);
        map.put("isTrue",b);
        map.put("timeStamp",new Date().getTime());
        map.put("server",serverPort);
        return new JSONObject(map);
    }
}
