package com.wenthor.batchJShell.service;

import com.wenthor.batchJShell.model.Notification;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class MailService{
    private Notification notification;
    private final JavaMailSender mailSender;
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public MailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendMail(){
        StringBuilder sb = new StringBuilder();
        sb.append("<p style=\"font-size: 16px;\"><b>Hello,</b></p>")
                .append("<p>The job you ran has successfully completed the record operation on the targeted database table.</p>")
                .append("<p style=\"font-size: 14px;\"><b>Details:</b></p>")
                .append("<ul>")
                .append("<li><b>File Name:</b> ").append(this.getNotification().getFileName()).append("</li>")
                .append("<li><b>Start Time:</b> ").append(this.getNotification().getJobStartTime()).append("</li>")
                .append("<li><b>End Time:</b> ").append(this.getNotification().getJobEndTime()).append("</li>")
                .append("</ul>");
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        final ClassPathResource resource = new ClassPathResource("csv/".concat(this.getNotification().getFileName()).concat(".csv"));
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setFrom("noreply@realworld.com");
            helper.setTo(this.getNotification().getUserMail());
            helper.setSubject(this.getNotification().getFileName().concat(".csv JOB FINISHED!"));
            helper.addAttachment(resource.getFilename(),resource.getFile());
            helper.setText(sb.toString(),true);
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            log.warn(e.getMessage());
        } catch (IOException e) {
            log.warn(e.getMessage());
        }
    }

    public Notification getNotification() {
        return notification;
    }
    public void createNotification(Notification notification) {
        this.notification = notification;
    }
}
