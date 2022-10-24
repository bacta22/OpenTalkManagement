package com.ncc.asia.service.EmailService;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.Map;

public interface EmailService {

    void sendEmailUsingThymeleafTemplate ()
            throws IOException, MessagingException;

    void sendEmailWithAttachment() throws MessagingException;
}
