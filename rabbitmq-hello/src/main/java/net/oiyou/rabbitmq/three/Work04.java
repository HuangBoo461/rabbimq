package net.oiyou.rabbitmq.three;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import net.oiyou.rabbitmq.utils.RabbitMqUtil;
import net.oiyou.rabbitmq.utils.SleepUtils;

/**
 * @author HuangBoo
 * @since 2023年04月30日 21:48
 */
public class Work04 {
    public static final String TASK_QUEUE_NAME = "ack_queue";

    public static void main(String[] args) throws Exception{
        Channel channel = RabbitMqUtil.getChannel();
        System.out.println("C2等待接受消息处理,耗时较长");


        //声明 接收消息
        DeliverCallback deliverCallback = (consumerTag, message) ->{
            SleepUtils.sleep(30);
            System.out.println("接收到的消息：" + new String(message.getBody(),"UTF-8"));
            //手动应答
            /**
             * 1、消息的标记 tatg
             * 2、是否批量应答
             */
            channel.basicAck(message.getEnvelope().getDeliveryTag(),false);
        };
        // 取消消息时回调
        CancelCallback cancelCallback = consumerTag -> {
            System.out.println(consumerTag + "消息者取消消极消费接口回调逻辑");
        };
        //设置不公平分发
//        int prefetchCount = 1;
        int prefetchCount = 5;
        channel.basicQos(prefetchCount);
        boolean autoAck = false;
        channel.basicConsume(TASK_QUEUE_NAME,autoAck,deliverCallback,cancelCallback);
    }
}
