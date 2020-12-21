package com.zfw.core.exception;

import com.alibaba.fastjson.JSONObject;
import com.zfw.core.constant.Constant;
import com.zfw.core.constant.ReturnCode;
import com.zfw.core.sys.entity.Log;
import com.zfw.core.sys.service.ILogService;
import com.zfw.utils.ReturnUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Author:zfw
 * @Date:2019/7/11
 * @Content:
 */
@ResponseBody
@ControllerAdvice
public class ControllerAdviceException {

    @Autowired
    private ILogService logService;

    @ExceptionHandler(value = Throwable.class)
    public JSONObject errorHandle(Throwable e){
        Log log = new Log().setType("2");
        if (e instanceof GlobalException){
            GlobalException ge=(GlobalException)e;
            if (ge.getConstant()!=null){
                log.setException(ge.toString()).setTitle(ge.getZhCode());
                logService.save(log);
                return ReturnUtils.fail(new ReturnCode(ge.getConstant().CODE,ge.getConstant().ZH_CODE,ge.getConstant().EN_CODE),null);
            }else {
                log.setException(ge.toString()).setTitle(ge.getZhCode());
                logService.save(log);
                return ReturnUtils.fail(new ReturnCode(ge.getCode(),ge.getZhCode(),ge.getEnCode()),null);
            }
        }
        else if(e instanceof NullPointerException){
            e.printStackTrace();
            log.setException(e.toString()).setTitle("后台空指针异常");
            logService.save(log);
            return ReturnUtils.fail(Constant.FAIL,"后台空指针异常");
        }
        else if(e instanceof SerializationException){
            e.printStackTrace();
            log.setType("2").setException(e.toString()).setTitle("缓存数据库异常，请重新登录");
            logService.save(log);
            return ReturnUtils.fail(Constant.FAIL,"缓存数据库异常，请重新登录");
        }
        else if (e instanceof BindException) {
            e.printStackTrace();
            log.setException(e.toString()).setTitle("参数绑定错误");
            logService.save(log);
            return ReturnUtils.fail(Constant.FAIL,"参数绑定错误");
        }
        else if(e.getMessage().toLowerCase().contains("redis")){
            e.printStackTrace();
            log.setException(e.toString()).setTitle("连接不上缓存数据库");
            logService.save(log);
            return ReturnUtils.fail(Constant.FAIL,"连接不上缓存数据库");
        }
        else{
            e.printStackTrace();
            log.setException(e.toString()).setTitle(HttpStatus.METHOD_NOT_ALLOWED.getReasonPhrase());
            logService.save(log);
            return ReturnUtils.fail(HttpStatus.METHOD_NOT_ALLOWED.value(),e.getMessage());
        }
    }
}
