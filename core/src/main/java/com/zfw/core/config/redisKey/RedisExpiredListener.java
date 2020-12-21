package com.zfw.core.config.redisKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

@Configuration
public class RedisExpiredListener implements MessageListener {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    /**
     * redis监听器
     * 针对redis数据失效事件，进行数据处理
     * @param message
     * @param
     */
    @Override
    public void onMessage(Message message, byte[] bytes) {
        // 用户做自己的业务处理即可,注意message.toString()可以获取失效的key
        String expiredKey = message.toString();
        logger.warn("key:{},失效了",expiredKey);
    }
}
