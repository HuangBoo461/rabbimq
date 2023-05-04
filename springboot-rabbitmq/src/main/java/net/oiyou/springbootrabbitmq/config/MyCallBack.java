package net.oiyou.springbootrabbitmq.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author HuangBoo
 * @since 2023年05月04日 15:41
 */
@Slf4j
@Component
public class MyCallBack implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnCallback {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @PostConstruct
    public void init() {
        rabbitTemplate.setConfirmCallback(this);
        rabbitTemplate.setReturnCallback(this);
    }

    /*
        交换机确认回调方法
        1、发消息 交换机接收到了 回调
        1.1 correlationData 保存回调消息的id及相关消息
        1.2 交换机收到消息 true
        1.3 cause null
        2.发消息 交换机接收失败 回调
        2.1 correlationData 保存回调消息的id及相关消息
        2.2 交换机收到消息 false
        1.3 cause 失败原因
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        String id = correlationData != null ? correlationData.getId() : "";
        if (ack) {
            log.info("发送交换机成功 id==={}", id);
        } else {
            log.info("发送交换机失败 id==={}，失败原因是：{}", id, cause);
        }
    }

    // 当消息传递过程中不可达目的地时将消息退回给生产者
    //之后又不可达目的地时才会回退
    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {

        log.error("消息:{}被交换机:{}退回，退回原因是：{}，routingKey是：{}", new String(message.getBody()), exchange, replyText, routingKey);
    }
}
