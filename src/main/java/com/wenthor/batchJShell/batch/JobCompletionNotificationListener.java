package com.wenthor.batchJShell.batch;

import com.wenthor.batchJShell.model.Notification;
import com.wenthor.batchJShell.service.BatchService;
import com.wenthor.batchJShell.service.rabbitmq.producer.NotificationProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

public class JobCompletionNotificationListener implements JobExecutionListener {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final BatchService batchService;
    private final NotificationProducer notificationProducer;

    public JobCompletionNotificationListener(BatchService batchService, NotificationProducer notificationProducer) {
        this.batchService = batchService;
        this.notificationProducer = notificationProducer;
    }

    @Override
    public void beforeJob(JobExecution jobExecution) {
        if (jobExecution.getStatus() == BatchStatus.STARTED)
            log.info("JOB STARTED !!");
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        if (jobExecution.getStatus() == BatchStatus.COMPLETED){
            Notification notification = new Notification(this.batchService.getMail(),this.batchService.getFileName(), jobExecution.getStartTime(),jobExecution.getEndTime());
            this.notificationProducer.sendToQueue(notification);
            log.info("JOB FINISHED !!");
        }
    }
}
