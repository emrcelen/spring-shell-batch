package com.wenthor.batchJShell.service.rabbitmq.producer;

import com.wenthor.batchJShell.model.Notification;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class NotificationProducer {
    @Value("${batch.rabbit.routing}")
    private String routingName;
    @Value("${batch.rabbit.exchange}")
    private String exchangeName;

    private final RabbitTemplate rabbitTemplate;

    public NotificationProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendToQueue(Notification notification){
        rabbitTemplate.convertAndSend(exchangeName,routingName,notification);
    }
}
