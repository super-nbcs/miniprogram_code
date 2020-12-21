package com.zfw.rabbit.receiver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

import java.io.IOException;

/**
 * 消息消费者1
 * @date 2019/3/1 17:32
 */
//@Component
public class FirstConsumer extends BaseConsumer {
    private Logger logger= LoggerFactory.getLogger(this.getClass());
    /**
     * queues  指定从哪个队列（queue）订阅消息
     * @param message
     * @throws IOException
     */
    @RabbitListener(queues = {"t_notice"})
    public void handleMessage1(Message message) throws IOException{
        logger.error("消费1");
        super.Run(message);
    }
    /**
     * queues  指定从哪个队列（queue）订阅消息
     * @param message
     * @throws IOException
     */
    @RabbitListener(queues = {"t_notice"})
    public void handleMessage2(Message message) throws IOException{
        logger.error("消费2");
        super.Run(message);
    }
    /**
     * queues  指定从哪个队列（queue）订阅消息
     * @param message
     * @throws IOException
     */
    @RabbitListener(queues = {"t_notice"})
    public void handleMessage3(Message message) throws IOException{
        logger.error("消费3");
        super.Run(message);
    }
    /**
     * queues  指定从哪个队列（queue）订阅消息
     * @param message
     * @throws IOException
     */
    @RabbitListener(queues = {"t_notice"})
    public void handleMessage4(Message message) throws IOException{
        logger.error("消费4");
        super.Run(message);
    }
    /**
     * queues  指定从哪个队列（queue）订阅消息
     * @param message
     * @throws IOException
     */
    @RabbitListener(queues = {"t_notice"})
    public void handleMessage5(Message message) throws IOException{
        logger.error("消费5");
        super.Run(message);
    }
    /**
     * queues  指定从哪个队列（queue）订阅消息
     * @param message
     * @throws IOException
     */
    @RabbitListener(queues = {"t_notice"})
    public void handleMessage6(Message message) throws IOException{
        logger.error("消费6");
        super.Run(message);
    }
    /**
     * queues  指定从哪个队列（queue）订阅消息
     * @param message
     * @throws IOException
     */
    @RabbitListener(queues = {"t_notice"})
    public void handleMessage7(Message message) throws IOException{
        logger.error("消费7");
        super.Run(message);
    }
    /**
     * queues  指定从哪个队列（queue）订阅消息
     * @param message
     * @throws IOException
     */
    @RabbitListener(queues = {"t_notice"})
    public void handleMessage8(Message message) throws IOException{
        logger.error("消费8");
        super.Run(message);
    }

    /**
     * queues  指定从哪个队列（queue）订阅消息
     * @param message
     * @throws IOException
     */
    @RabbitListener(queues = {"t_notice"})
    public void handleMessage9(Message message) throws IOException{
        logger.error("消费9");
        super.Run(message);
    }
    /**
     * queues  指定从哪个队列（queue）订阅消息
     * @param message
     * @throws IOException
     */
    @RabbitListener(queues = {"t_notice"})
    public void handleMessage10(Message message) throws IOException{
        logger.error("消费10");
        super.Run(message);
    }
    /**
     * queues  指定从哪个队列（queue）订阅消息
     * @param message
     * @throws IOException
     */
    @RabbitListener(queues = {"t_notice"})
    public void handleMessage11(Message message) throws IOException{
        logger.error("消费11");
        super.Run(message);
    }
    /**
     * queues  指定从哪个队列（queue）订阅消息
     * @param message
     * @throws IOException
     */
    @RabbitListener(queues = {"t_notice"})
    public void handleMessage12(Message message) throws IOException{
        logger.error("消费12");
        super.Run(message);
    }
    /**
     * queues  指定从哪个队列（queue）订阅消息
     * @param message
     * @throws IOException
     */
    @RabbitListener(queues = {"t_notice"})
    public void handleMessage13(Message message) throws IOException{
        logger.error("消费13");
        super.Run(message);
    }
    @RabbitListener(queues = {"t_notice"})
    public void handleMessage14(Message message) throws IOException{
        logger.error("消费14");
        super.Run(message);
    }
    /**
     * queues  指定从哪个队列（queue）订阅消息
     * @param message
     * @throws IOException
     */
    @RabbitListener(queues = {"t_notice"})
    public void handleMessage15(Message message) throws IOException{
        logger.error("消费15");
        super.Run(message);
    }
    @RabbitListener(queues = {"t_notice"})
    public void handleMessage16(Message message) throws IOException{
        logger.error("消费16");
        super.Run(message);
    }
    @RabbitListener(queues = {"t_notice"})
    public void handleMessage17(Message message) throws IOException{
        logger.error("消费17");
        super.Run(message);
    }

}
