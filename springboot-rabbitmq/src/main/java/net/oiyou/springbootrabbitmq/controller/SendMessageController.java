package net.oiyou.springbootrabbitmq.controller;

import lombok.extern.slf4j.Slf4j;
import net.oiyou.springbootrabbitmq.config.DelayExchangeConfig;
import net.oiyou.springbootrabbitmq.consumer.DeadLetterQueueConsumer;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * @author HuangBoo
 * @since 2023年05月03日 23:44
 */
@Slf4j
@RestController
@RequestMapping("/ttl")
public class SendMessageController {

    private final RabbitTemplate rabbitTemplate;

    public SendMessageController(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @GetMapping("/sendMessage/{message}")
    public void sendMessage(@PathVariable String message) {
        log.info("当前时间:{},发送一条消息{}", new Date(), message);
        rabbitTemplate.convertAndSend("X", "XA", "消息来自ttl为10s的队列" + message);
        rabbitTemplate.convertAndSend("X", "XB", "消息来自ttl为40s的队列" + message);
    }

    @GetMapping("/sendExpirationMsg/{message}/{ttlTime}")
    public void sendExpirationMsg(@PathVariable String message, @PathVariable String ttlTime) {
        log.info("当前时间:{},发送一条时长:{}毫秒 消息：{}", new Date(), ttlTime, message);
        rabbitTemplate.convertAndSend("X", "XC", message,msg ->{
            msg.getMessageProperties().setExpiration(ttlTime);
            return msg;
        });
    }

    @GetMapping("/sendDelayedMsg/{message}/{delayedTime}")
    public void sendExpirationMsg(@PathVariable String message, @PathVariable Integer delayedTime) {
        log.info("当前时间:{},发送一条延时:{}毫秒 消息：{}", new Date(), delayedTime, message);
        rabbitTemplate.convertAndSend(DelayExchangeConfig.DELAYED_EXCHANGE, DelayExchangeConfig.DELAYED_ROUTING_KEY, message, msg ->{
            msg.getMessageProperties().setDelay(delayedTime);
            return msg;
        });
    }
}
