package net.oiyou.springbootrabbitmq.controller;

import lombok.extern.slf4j.Slf4j;
import net.oiyou.springbootrabbitmq.config.ConfirmConfig;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author HuangBoo
 * @since 2023年05月04日 15:36
 */
@Slf4j
@RestController
@RequestMapping
public class ConfirmController {

    private final RabbitTemplate rabbitTemplate;

    public ConfirmController(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }


    @GetMapping("/sendMessage/{message}")
    public void sendMessage(@PathVariable String message) {
        CorrelationData correlationData = new CorrelationData("1");
        log.info("发送确认消息：{}", message);
        rabbitTemplate.convertAndSend(ConfirmConfig.CONFIRM_EXCHANGE, ConfirmConfig.CONFIRM_ROUTING_KEY + "6", message, correlationData);
    }
}
