package net.oiyou.rabbitmq.two;

import com.rabbitmq.client.Channel;
import net.oiyou.rabbitmq.utils.RabbitMqUtil;

import java.util.Scanner;

/**
 * @author HuangBoo
 * @since 2023年04月30日 19:59
 */
public class Task01 {
    public static final String QUEUE_NAME = "hello";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtil.getChannel();
        /**
         * 生成队列
         * 1、队列名称
         * 2、队列里面的消息是否需要持久化(磁盘),默认情况消息保存在内存中
         * 3、该队列是或否只提供给一个消费者进行消费，是否进行消息共享，true可以多个消费者消费，false只能一个消费者进行消费
         * 4、是否自动删除，最后一个消费者断开连接以后，该队列是否自动删除，true代表自动删除
         * 5、其他参数
         */
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNext()) {
            String next = scanner.next();
            /**
             * 1、发送到那个交换机
             * 2、发送到哪个路由，，次即为queue_name
             * 3、其他参数
             * 4、发送消息内容
             */
            channel.basicPublish("", QUEUE_NAME, null, next.getBytes());
            System.out.println("发送消息完成:" + next);
        }
    }
}
