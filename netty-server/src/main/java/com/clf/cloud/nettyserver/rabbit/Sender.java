package com.clf.cloud.nettyserver.rabbit;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @Author: clf
 * @Date: 2020-04-12
 * @Description: TODO
 */
@Component
public class Sender {

    @Autowired
    RabbitTemplate rabbitTemplate;
    public void send(String message) {
        rabbitTemplate.convertAndSend("immediate_exchange_test1", "immediate_routing_key_test1", message);
    }
}
