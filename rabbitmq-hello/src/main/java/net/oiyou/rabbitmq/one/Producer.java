package net.oiyou.rabbitmq.one;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author HuangBoo
 * @since 2023年04月30日 12:15
 */
public class Producer {
    public static final String QUEUE_NAME = "hello";

    public static void main(String[] args) throws Exception{
        //创建连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        //设置ip
        factory.setHost("192.168.3.83");
        //设用户名
        factory.setUsername("guest");
        //设置密码
        factory.setPassword("guest");
        //创建连接
        Connection connection = factory.newConnection();
        //创建信道
        Channel channel = connection.createChannel();
        /**
         * 生成队列
         * 1、队列名称
         * 2、队列里面的消息是否需要持久化(磁盘),默认情况消息保存在内存中
         * 3、该队列是或否只提供给一个消费者进行消费，是否进行消息共享，true可以多个消费者消费，false只能一个消费者进行消费
         * 4、是否自动删除，最后一个消费者断开连接以后，该队列是否自动删除，true代表自动删除
         * 5、其他参数
         */
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);
        //发送消息
        String message = "hello world";
        /**
         * 1、发送到那个交换机
         * 2、发送到哪个路由，，次即为queue_name
         * 3、其他参数
         * 4、发送消息内容
         */
        channel.basicPublish("",QUEUE_NAME,null,message.getBytes());

        System.out.println("发送消息完毕");
    }
}
