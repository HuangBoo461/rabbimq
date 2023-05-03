package net.oiyou.rabbitmq.eight;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import net.oiyou.rabbitmq.utils.RabbitMqUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * @author HuangBoo
 * @since 2023年05月01日 21:06
 */
public class Consumer02  {
    public static final String DEAD_QUEUE = "dead_queue";


    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtil.getChannel();

        DeliverCallback deliverCallback = (consumerTag, message) -> {
            String msg = new String(message.getBody(), "UTF-8");
            System.out.println("Consumer02控制台接收到消息：" + msg);
        };
        channel.basicConsume(DEAD_QUEUE, true, deliverCallback, consumerTag -> {
        });
    }
}
