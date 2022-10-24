package com.ncc.asia.schedule;

import com.ncc.asia.controller.MailRestController;
import com.ncc.asia.service.EmailService.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import java.io.IOException;

@Component
@Slf4j
public class ScheduleTask {

    @Autowired
    private EmailService emailService;

    @Scheduled(cron = "0 */2 * ? * *")
    public void sendEmail () throws MessagingException, IOException {
        emailService.sendEmailUsingThymeleafTemplate();
        log.info("Send email ");
    }

}
