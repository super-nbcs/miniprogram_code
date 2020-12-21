package com.zfw.rabbit.sender;

import com.zfw.rabbit.config.RabbitMqConfig;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class YiTuSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 发送消息
     * @param uuid
     * @param message  消息
     */
    public void send(String uuid,Object message) {
        CorrelationData correlationId = new CorrelationData(uuid);
        /**
         * RabbitMqConfig.EXCHANGE  指定消息交换机
         * RabbitMqConfig.koalaQueueKey  指定队列key2
         */
        rabbitTemplate.convertAndSend(RabbitMqConfig.EXCHANGE, RabbitMqConfig.yiTuQueueKey,
                message, correlationId);
    }
}
