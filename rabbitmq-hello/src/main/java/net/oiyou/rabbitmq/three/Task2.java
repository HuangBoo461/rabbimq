package net.oiyou.rabbitmq.three;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;
import net.oiyou.rabbitmq.utils.RabbitMqUtil;

import java.util.Scanner;

/**
 * 消息在手动应答时不丢失，放回队列重新消费
 *
 * @author HuangBoo
 * @since 2023年04月30日 21:37
 */
public class Task2 {
    public static final String TASK_QUEUE_NAME = "ack_queue";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtil.getChannel();

        boolean durable = true;
        channel.queueDeclare(TASK_QUEUE_NAME,durable,false,false,null);

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()){
            String message = scanner.next();
            //生产者发送消息为持久化消息(要求保存到磁盘上)，不设置时默认保存在内存中
            channel.basicPublish("",TASK_QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN,message.getBytes("UTF-8"));
            System.out.println("生产者发送消息:" + message);
        }
    }
}
