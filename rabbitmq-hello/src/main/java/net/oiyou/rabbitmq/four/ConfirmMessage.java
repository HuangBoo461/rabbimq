package net.oiyou.rabbitmq.four;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;
import net.oiyou.rabbitmq.utils.RabbitMqUtil;

import java.util.UUID;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * 发布确认模式
 * 使用时间 比较哪种确认方式进行比较
 * 1、单个确认
 * 2、批量确认
 * 3、异步批量确认
 *
 * @author HuangBoo
 * @since 2023年05月01日 0:05
 */
public class ConfirmMessage {

    public static final int MESSAGE_COUNT = 10000;


    public static void main(String[] args) throws Exception {
        //1、单个发布确认 单发布确认模式下发送1000个消耗时382MS  单发布确认模式下发送10000个消耗时2695MS
//        publishMessageSingleConfirm();
        //2、批量发布确认 批量发布确认模式下发送1000个消耗时62MS  批量发布确认模式下发送10000个消耗时489MS
//        publishMessageBatch();
        //3、批量异步发布确认  步发布确认模式下发送1000个消耗时61MS  异步发布确认模式下发送10000个消耗时132MS
        publishMessageAsync();
    }


    public static void publishMessageSingleConfirm() throws Exception {
        String queueName = UUID.randomUUID().toString();
        Channel channel = RabbitMqUtil.getChannel();
        channel.queueDeclare(queueName, true, false, false, null);
        //发布确认
        channel.confirmSelect();

        long begin = System.currentTimeMillis();
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = i + "";
            channel.basicPublish("",queueName,null,message.getBytes());
            boolean flag = channel.waitForConfirms();
            if (flag) {
                System.out.println("发送" + i + "成功");
            }
        }
        long end = System.currentTimeMillis();
        System.out.println("单发布确认模式下发送" + MESSAGE_COUNT + "个消耗时" + (end - begin) + "MS");
    }


    public static void publishMessageBatch() throws Exception {
        String queueName = UUID.randomUUID().toString();
        Channel channel = RabbitMqUtil.getChannel();
        channel.queueDeclare(queueName, true, false, false, null);
        //发布确认
        channel.confirmSelect();

        int batchSize = 100;

        long begin = System.currentTimeMillis();
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = i + "";
            channel.basicPublish("",queueName,null,message.getBytes());
            if(i % batchSize == 99){
                boolean flag = channel.waitForConfirms();
                if (flag) {
                    System.out.println("发送至" + i + "成功");
                }
            }
        }
        long end = System.currentTimeMillis();
        System.out.println("批量发布确认模式下发送" + MESSAGE_COUNT + "个消耗时" + (end - begin) + "MS");
    }


    public static void publishMessageAsync() throws Exception {
        String queueName = UUID.randomUUID().toString();
        Channel channel = RabbitMqUtil.getChannel();
        channel.queueDeclare(queueName, true, false, false, null);
        //发布确认
        channel.confirmSelect();

        /**
         *线程安全有序的一个哈希表，适用于高并发的i情况下
         * 1、轻松的将序号与i消息进行关联
         * 2、轻松的批量删除条目 只要给到序号
         * 3、支持高并发
         */
        ConcurrentSkipListMap<Long,String> confirmMessages = new ConcurrentSkipListMap<>();

        //消息成功回调
        ConfirmCallback ackCallback = (deliveryTag, multiple) ->{
            if(multiple){
                ConcurrentNavigableMap<Long, String> longStringConcurrentNavigableMap =
                        confirmMessages.headMap(deliveryTag);
                longStringConcurrentNavigableMap.clear();
            }else {
                confirmMessages.remove(deliveryTag);
            }
            System.out.println("确认消息:" + deliveryTag);
        };
        //消息失败回调
        /**
         * 1、消息的标记
         * 2、是否为批量确认
         */
        ConfirmCallback nackCallback = (deliveryTag, multiple) -> {
            String message = confirmMessages.get(deliveryTag);
            System.out.println("未确认消息:" + message + ":::::::未确认消息tag:" + deliveryTag);
        };
        channel.addConfirmListener(ackCallback,nackCallback);
        long begin = System.currentTimeMillis();
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = i + "";
            channel.basicPublish("",queueName,null,message.getBytes());
            confirmMessages.put(channel.getNextPublishSeqNo(),message);
        }
        long end = System.currentTimeMillis();
        System.out.println("异步发布确认模式下发送" + MESSAGE_COUNT + "个消耗时" + (end - begin) + "MS");
    }
}

