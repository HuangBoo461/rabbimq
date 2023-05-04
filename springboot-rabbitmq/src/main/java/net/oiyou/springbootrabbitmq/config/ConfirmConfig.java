package net.oiyou.springbootrabbitmq.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author HuangBoo
 * @since 2023年05月04日 15:28
 */
@Slf4j
@Configuration
public class ConfirmConfig {
    public static final String CONFIRM_EXCHANGE = "confirm.exchange";
    public static final String CONFIRM_QUEUE = "confirm.queue";
    public static final String CONFIRM_ROUTING_KEY = "key1";

    //备份交换机
    public static final String BACKUP_EXCHANGE = "backup.exchange";
    //备份队列
    public static final String BACKUP_QUEUE = "backup.queue";
    //报警队列
    public static final String WARNING_QUEUE = "warning.queue";

    @Bean
    public DirectExchange confirmExchange() {
        return ExchangeBuilder.directExchange(CONFIRM_EXCHANGE).durable(true)
                .withArgument("alternate-exchange", BACKUP_EXCHANGE).build();
    }

    @Bean
    public FanoutExchange backupExchange() {
        return ExchangeBuilder.fanoutExchange(BACKUP_EXCHANGE).build();
    }

    @Bean
    public Queue confirmQueue() {
        return QueueBuilder.durable(CONFIRM_QUEUE).build();
    }

    @Bean
    public Queue backupQueue() {
        return QueueBuilder.durable(BACKUP_QUEUE).build();
    }

    @Bean
    public Queue warningQueue() {
        return QueueBuilder.durable(WARNING_QUEUE).build();
    }

    @Bean
    public Binding backupBinding(@Qualifier("backupExchange") FanoutExchange backupExchange,
                                 @Qualifier("backupQueue") Queue backupQueue) {
        return BindingBuilder.bind(backupQueue).to(backupExchange);
    }

    @Bean
    public Binding warningBinding(@Qualifier("backupExchange") FanoutExchange backupExchange,
                                  @Qualifier("warningQueue") Queue warningQueue) {
        return BindingBuilder.bind(warningQueue).to(backupExchange);
    }

    @Bean
    public Binding confirmBinding(@Qualifier("confirmExchange") DirectExchange confirmExchange,
                                  @Qualifier("confirmQueue") Queue confirmQueue) {
        return BindingBuilder.bind(confirmQueue).to(confirmExchange).with(CONFIRM_ROUTING_KEY);
    }

    //    @PostConstruct
    public void build(RabbitTemplate rabbitTemplate) {
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            String id = correlationData != null ? correlationData.getId() : "";
            if (ack) {
                log.info("发送交换机成功 id==={}", id);
            } else {
                log.info("发送交换机失败 id==={}，失败原因是：{}", id, cause);
            }
        });
    }
}
