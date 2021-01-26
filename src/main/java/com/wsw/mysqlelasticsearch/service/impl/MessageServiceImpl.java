package com.wsw.mysqlelasticsearch.service.impl;

import com.wsw.mysqlelasticsearch.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;
import java.util.UUID;

/**
 * @Author WangSongWen
 * @Date: Created in 14:48 2021/1/25
 * @Description: 消息发送主服务
 */
@Service
@Slf4j
public class MessageServiceImpl implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnCallback, MessageService {
    @Resource
    private RabbitTemplate rabbitTemplate;

    @Override
    public void sendMessage(Map<String, Object> message) {
        rabbitTemplate.setConfirmCallback(this);
        rabbitTemplate.setReturnCallback(this);
        CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
        rabbitTemplate.convertAndSend("blogExchange", "", message, correlationData);
    }

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        System.out.println("消息ID: " + (correlationData != null ? correlationData.getId() : null));
        if (ack) {
            log.info("消息发送成功: {}", correlationData);
        } else {
            log.info("消息发送失败: {}", cause);
        }
    }

    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
        log.info("消息主体: {}", message);
        log.info("应答码: {}", replyCode);
        log.info("描述: {}", replyText);
        log.info("消息使用的交换器 exchange: {}", exchange);
        log.info("消息使用的路由键 routing: {}", routingKey);
    }
}