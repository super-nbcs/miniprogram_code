package com.zfw.core.init;

import com.zfw.core.constant.CacheName;
import com.zfw.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @Author:zfw
 * @Date:2020-08-12
 * @Content:
 */
@Component
@Order(value = 0)
public class ApplicationInit implements ApplicationRunner {
    @Autowired
    private RedisUtils redisUtils;
    @Override
    public void run(ApplicationArguments args) throws Exception {
        clearServiceCache();
    }


    /**
     * 清理服务接口缓存
     */
    public void clearServiceCache(){
        redisUtils.delete(CacheName.SERVICE_INTERFACE);
    }
}
