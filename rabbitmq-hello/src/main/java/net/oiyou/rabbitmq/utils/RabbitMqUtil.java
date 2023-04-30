package net.oiyou.rabbitmq.utils;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * rabbitma工具类
 *
 * @author HuangBoo
 * @since 2023年04月30日 19:43
 */
public class RabbitMqUtil {
    public static Channel getChannel() throws Exception {
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

        return channel;
    }
}
