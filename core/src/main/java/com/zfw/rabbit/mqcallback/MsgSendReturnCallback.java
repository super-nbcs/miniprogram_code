package com.zfw.rabbit.mqcallback;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.io.UnsupportedEncodingException;

public class MsgSendReturnCallback implements RabbitTemplate.ReturnCallback {
    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
        try {
            System.out.println("return--message:" + new String(message.getBody(), "UTF-8") + ",replyCode:" + replyCode
                    + ",replyText:" + replyText + ",exchange:" + exchange + ",routingKey:" + routingKey);
        } catch (UnsupportedEncodingException e) {
        }
    }
}
