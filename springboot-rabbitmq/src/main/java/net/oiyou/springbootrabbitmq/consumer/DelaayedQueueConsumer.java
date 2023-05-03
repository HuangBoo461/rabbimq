package net.oiyou.springbootrabbitmq.consumer;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import net.oiyou.springbootrabbitmq.config.DelayExchangeConfig;
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
public class DelaayedQueueConsumer {
    @RabbitListener(queues = DelayExchangeConfig.DELAYED_QUEUE)
    public void receiveD(Message message) {
        String msg = new String(message.getBody());
        log.info("当前时间:{},收到延时消息:{}", new Date(), msg);
    }
}
