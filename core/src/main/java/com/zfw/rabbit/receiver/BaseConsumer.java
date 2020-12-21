package com.zfw.rabbit.receiver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.stereotype.Component;

import java.io.IOException;


@Component
public class BaseConsumer {
    private Logger logger= LoggerFactory.getLogger(this.getClass());

    public void Run(Message message) throws IOException {

    }

}
