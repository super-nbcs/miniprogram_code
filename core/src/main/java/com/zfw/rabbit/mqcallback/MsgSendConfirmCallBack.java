package com.zfw.rabbit.mqcallback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;


public class MsgSendConfirmCallBack implements RabbitTemplate.ConfirmCallback {

    private Logger logger= LoggerFactory.getLogger(this.getClass());
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        logger.info("MsgSendConfirmCallBack  , 回调id:" + correlationData);
        if (ack) {
            logger.info("消息储存成功");
        } else {
            logger.info("消息储存成功:" + cause+"\n重新发送");
        }
    }
}
