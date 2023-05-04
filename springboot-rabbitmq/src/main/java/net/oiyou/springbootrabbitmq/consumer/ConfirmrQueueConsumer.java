package net.oiyou.springbootrabbitmq.consumer;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import net.oiyou.springbootrabbitmq.config.ConfirmConfig;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author HuangBoo
 * @since 2023年05月03日 23:51
 */
@Slf4j
@Component
public class ConfirmrQueueConsumer {
    @RabbitListener(queues = ConfirmConfig.CONFIRM_QUEUE)
    public void receiveD(Message message, Channel channel) {
        String msg = new String(message.getBody());
        log.info("当前时间:{},收到确认消息:{}", new Date(), msg);
    }
}
