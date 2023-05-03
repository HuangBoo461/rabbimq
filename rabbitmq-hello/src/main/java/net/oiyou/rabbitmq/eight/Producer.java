package net.oiyou.rabbitmq.eight;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import net.oiyou.rabbitmq.utils.RabbitMqUtil;
import net.oiyou.rabbitmq.utils.SleepUtils;

/**
 * @author HuangBoo
 * @since 2023年05月01日 21:40
 */
public class Producer {
    public static final String NORMAL_EXCHANGE = "normal_exchange";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtil.getChannel();
        //死信消息 设置TTL时间 单位时毫秒，
        /*AMQP.BasicProperties properties = new AMQP.BasicProperties()
                .builder().expiration("10000").build();*/
        for (int i = 1; i < 11; i++) {
            String message = "info" + i;
            channel.basicPublish(NORMAL_EXCHANGE,"zhangsan",null,message.getBytes());
            //SleepUtils.sleep(1);
        }
    }
}
