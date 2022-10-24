package com.ncc.asia.controller;

import com.ncc.asia.service.EmailService.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.mail.MessagingException;
import java.io.IOException;

@RestController
@RequestMapping("/api/email")
public class MailRestController {

    @Autowired
    private EmailService emailService;

    @GetMapping("/send")
    public ResponseEntity<String> send () throws MessagingException, IOException {
        emailService.sendEmailUsingThymeleafTemplate();
        return new ResponseEntity<String>("Send mail successfully", HttpStatus.OK);
    }

    @GetMapping("/sendSimple")
    public ResponseEntity<String> sendSimpleMail () throws MessagingException {
        emailService.sendEmailWithAttachment();
        return new ResponseEntity<String>("Send mail successfully", HttpStatus.OK);
    }
}
