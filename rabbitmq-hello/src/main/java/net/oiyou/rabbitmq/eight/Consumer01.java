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
public class Consumer01 {

    public static final String NORMAL_EXCHANGE = "normal_exchange";
    public static final String DEAD_EXCHANGE = "dead_exchange";
    public static final String NORMAL_QUEUE = "normal_queue";
    public static final String DEAD_QUEUE = "dead_queue";


    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtil.getChannel();

        channel.exchangeDeclare(NORMAL_EXCHANGE, BuiltinExchangeType.DIRECT);
        channel.exchangeDeclare(DEAD_EXCHANGE, BuiltinExchangeType.DIRECT);

        //普通队列设置参数
        Map<String, Object> arguments = new HashMap<>(16);
        //arguments.put("x-message-ttl",10000); 可以在生产者指定消息过期时间
        arguments.put("x-dead-letter-exchange", DEAD_EXCHANGE);
        arguments.put("x-dead-letter-routing-key", "lisi");
        //设置正常消息队列长度
        //arguments.put("x-max-length", 6);
        //生命普通队列
        channel.queueDeclare(NORMAL_QUEUE, false, false, false, arguments);
        //声死信队列
        channel.queueDeclare(DEAD_QUEUE, false, false, false, null);

        channel.queueBind(NORMAL_QUEUE, NORMAL_EXCHANGE, "zhangsan");
        channel.queueBind(DEAD_QUEUE, DEAD_EXCHANGE, "lisi");


        DeliverCallback deliverCallback = (consumerTag, message) -> {
            String msg = new String(message.getBody(), "UTF-8");
            if(msg.equals("info5")){
                System.out.println("Consumer01控制台接收到消息：" + msg + "消息被拒绝");
                channel.basicReject(message.getEnvelope().getDeliveryTag(),false);

            }else{
                System.out.println("Consumer01控制台接收到消息：" + msg);
                channel.basicAck(message.getEnvelope().getDeliveryTag(),false);
            }
        };
        channel.basicConsume(NORMAL_QUEUE, false, deliverCallback, consumerTag -> {
        });
    }
}
