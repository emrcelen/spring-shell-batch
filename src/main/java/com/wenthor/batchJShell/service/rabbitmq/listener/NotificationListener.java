package com.wenthor.batchJShell.service.rabbitmq.listener;

import com.wenthor.batchJShell.model.Notification;
import com.wenthor.batchJShell.service.MailService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class NotificationListener {
    private final MailService mailService;

    public NotificationListener(MailService mailService) {
        this.mailService = mailService;
    }

    @RabbitListener(queues = "${batch.rabbit.queue}")
    public void handleMessage(Notification notification){
        mailService.createNotification(notification);
        mailService.sendMail();
    }
}
