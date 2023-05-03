package net.oiyou.springbootrabbitmq.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * TTL队列配置文件类
 * @author HuangBoo
 * @since 2023年05月03日 23:04
 */
@Configuration
public class TtlQueueConfig {

    public static final String X_EXCHANGE = "X";
    /**
     * 死信交换机
     *
     */
    public static final String Y_DEAD_LETTER_EXCHANGE = "Y";
    public static final String QUEUE_A = "QA";
    public static final String QUEUE_B = "QB";
    public static final String QUEUE_C = "QC";
    public static final String QUEUE_DEAD_D = "QD";

    @Bean("xExchange")
    public Exchange xExchange(){
        return ExchangeBuilder.directExchange(X_EXCHANGE).build();
    }

    @Bean("yExchange")
    public Exchange yExchange(){
        return ExchangeBuilder.directExchange(Y_DEAD_LETTER_EXCHANGE).build();
    }

    @Bean("queueA")
    public Queue queueA(){
        Map<String, Object> arguments = new HashMap<>(16);
        //设置死信交换机
        arguments.put("x-dead-letter-exchange",Y_DEAD_LETTER_EXCHANGE);
        //设置死信路由
        arguments.put("x-dead-letter-routing-key","YD");
        //设置过过期时间 时间单位ms
        arguments.put("x-message-ttl",10000);
        return QueueBuilder.durable(QUEUE_A).withArguments(arguments).build();
    }

    @Bean("queueB")
    public Queue queueB(){
        Map<String, Object> arguments = new HashMap<>(16);
        //设置死信交换机
        arguments.put("x-dead-letter-exchange",Y_DEAD_LETTER_EXCHANGE);
        //设置死信路由
        arguments.put("x-dead-letter-routing-key","YD");
        //设置过过期时间 时间单位ms
        arguments.put("x-message-ttl",40000);
        return QueueBuilder.durable(QUEUE_B).withArguments(arguments).build();
    }

    @Bean("queueC")
    public Queue queueC(){
        Map<String, Object> arguments = new HashMap<>(16);
        //设置死信交换机
        arguments.put("x-dead-letter-exchange",Y_DEAD_LETTER_EXCHANGE);
        //设置死信路由
        arguments.put("x-dead-letter-routing-key","YD");
        //设置过过期时间 时间单位ms
//        arguments.put("x-message-ttl",40000);
        return QueueBuilder.durable(QUEUE_C).withArguments(arguments).build();
    }

    @Bean("queueD")
    public Queue queueD(){
        return QueueBuilder.durable(QUEUE_DEAD_D).build();
    }

    @Bean
    public Binding queueABindingX(@Qualifier("queueA") Queue queueA,@Qualifier("xExchange") Exchange exchangeX){
        return BindingBuilder.bind(queueA).to(exchangeX).with("XA").noargs();
    }

    @Bean
    public Binding queueBBindingX(@Qualifier("queueB") Queue queueB,@Qualifier("xExchange") Exchange exchangeX){
        return BindingBuilder.bind(queueB).to(exchangeX).with("XB").noargs();
    }

    @Bean
    public Binding queueCBindingX(@Qualifier("queueC") Queue queueC,@Qualifier("xExchange") Exchange exchangeX){
        return BindingBuilder.bind(queueC).to(exchangeX).with("XC").noargs();
    }

    @Bean
    public Binding queueDBindingY(@Qualifier("queueD") Queue queueD,@Qualifier("yExchange") Exchange exchangeY){
        return BindingBuilder.bind(queueD).to(exchangeY).with("YD").noargs();
    }

}
