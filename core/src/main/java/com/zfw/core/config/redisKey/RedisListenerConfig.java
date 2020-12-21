package com.zfw.core.config.redisKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

/**
 * @Auther :
 * @Date :
 * @Description : redis监听器配置文件
 */
@Configuration
public class RedisListenerConfig {
    @Value("${spring.redis.database}")
    private Integer database;
    @Bean
    @Primary
    RedisMessageListenerContainer container(RedisConnectionFactory redisConnectionFactory){
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory);
        //下面这种方式是灵活配置，针对每个库的失效key做处理
        container.addMessageListener(new RedisExpiredListener(), new PatternTopic("__keyevent@"+database+"__:expired"));
        return container;
    }


}
