package net.oiyou.rabbitmq.six;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import net.oiyou.rabbitmq.utils.RabbitMqUtil;

/**
 * @author HuangBoo
 * @since 2023年05月01日 13:30
 */
public class ReceiveLogsDirect01 {

    public static final String EXCHANGE_NAME = "direct_logs";

    public static void main(String[] args) throws Exception{
        Channel channel = RabbitMqUtil.getChannel();
        //交换机名称，扇出类型
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        //生命一个队列 临时队列
        channel.queueDeclare("console",false,false,false,null);

        channel.queueBind("console", EXCHANGE_NAME, "info");
        channel.queueBind("console", EXCHANGE_NAME, "warning");

        System.out.println("等待消息接受,把接收到的消息打印到屏幕上");

        DeliverCallback deliverCallback = (consumerTag, message) -> {
            String msg = new String(message.getBody(), "UTF-8");
            System.out.println("ReceiveLogsDirect01控制台接收到消息：" + msg);
        };

        channel.basicConsume("console", true, deliverCallback, consumerTag -> {
        });
    }
}
