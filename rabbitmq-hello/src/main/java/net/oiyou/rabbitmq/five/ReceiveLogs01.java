package net.oiyou.rabbitmq.five;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import net.oiyou.rabbitmq.utils.RabbitMqUtil;

/**
 * @author HuangBoo
 * @since 2023年05月01日 12:59
 */
public class ReceiveLogs01 {

    public static final String EXCHANGE_NAME = "logs";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtil.getChannel();
        //交换机名称，扇出类型
        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
        //生命一个队列 临时队列
        String queueName = channel.queueDeclare().getQueue();

        channel.queueBind(queueName, EXCHANGE_NAME, "");
        System.out.println("等待消息接受,把接收到的消息打印到屏幕上");

        DeliverCallback deliverCallback = (consumerTag, message) -> {
            String msg = new String(message.getBody(), "UTF-8");
            System.out.println("ReceiveLogs01控制台接收到消息：" + msg);
        };

        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {
        });
    }

}
