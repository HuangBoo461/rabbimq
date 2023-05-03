package net.oiyou.springbootrabbitmq.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author HuangBoo
 * @since 2023年05月04日 0:41
 */
@Configuration
public class DelayExchangeConfig {

    public static final String DELAYED_EXCHANGE = "delayed.exchange";
    public static final String DELAYED_QUEUE = "delayed.queue";
    public static final String DELAYED_ROUTING_KEY = "delayed.routingkey";

    @Bean
    public CustomExchange delayedExchange(){
        Map<String, Object> arguments = new HashMap<>(16);
        arguments.put("x-delayed-type","direct");

        /*
         * 1.交换机名称
         * 2.交换机类型
         * 3.是否持久化
         * 4.是否自动删除
         * 5.其他参数
         */
        return new CustomExchange(DELAYED_EXCHANGE,"x-delayed-message",true,false,arguments);
    }

    @Bean
    public Queue delayedQueue(){
        return new Queue(DELAYED_QUEUE);
    }

    @Bean
    public Binding delayedBinding(@Qualifier("delayedQueue") Queue delayedQueue,
                                  @Qualifier("delayedExchange") CustomExchange delayedExchange){
        return BindingBuilder.bind(delayedQueue).to(delayedExchange).with(DELAYED_ROUTING_KEY).noargs();
    }
}
