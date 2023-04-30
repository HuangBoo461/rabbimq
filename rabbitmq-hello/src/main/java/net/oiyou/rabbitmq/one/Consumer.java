package net.oiyou.rabbitmq.one;

import com.rabbitmq.client.*;

/**
 * @author HuangBoo
 * @since 2023年04月30日 19:07
 */
public class Consumer {
    public static final String QUEUE_NAME = "hello";

    public static void main(String[] args) throws Exception {
        //创建连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        //设置ip
        factory.setHost("192.168.3.83");
        //设用户名
        factory.setUsername("guest");
        //设置密码
        factory.setPassword("guest");

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        //声明 接收消息
        DeliverCallback deliverCallback = (consumerTag, message) ->{
            System.out.println(new String(message.getBody()));
        };
        // 取消消息时回调
        CancelCallback cancelCallback = consumerTag -> {
            System.out.println("消费被中断");
        };

        /**
         * 1、消息队列
         * 2、消费成功后似乎否自动应答 true代表自动应答，false 代表手动应答
         * 3、消费未成功时回调
         * 4、消费者取消消时的回调
         */
        channel.basicConsume(QUEUE_NAME,true,deliverCallback,cancelCallback);

    }
}
