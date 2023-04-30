package net.oiyou.rabbitmq.two;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import net.oiyou.rabbitmq.utils.RabbitMqUtil;

/**
 * @author HuangBoo
 * @since 2023年04月30日 19:50
 */
public class Work01 {

    public static final String QUEUE_NAME = "hello";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtil.getChannel();

        //声明 接收消息
        DeliverCallback deliverCallback = (consumerTag, message) ->{
            System.out.println("接收到的消息：" + new String(message.getBody()));
        };
        // 取消消息时回调
        CancelCallback cancelCallback = consumerTag -> {
            System.out.println(consumerTag + "消息者取消消极消费接口回调逻辑");
        };

        /**
         * 1、消息队列
         * 2、消费成功后似乎否自动应答 true代表自动应答，false 代表手动应答
         * 3、消费未成功时回调
         * 4、消费者取消消时的回调
         */
        System.out.println("C2等待接收到消息。。。。");
        channel.basicConsume(QUEUE_NAME,true,deliverCallback,cancelCallback);

    }
}
