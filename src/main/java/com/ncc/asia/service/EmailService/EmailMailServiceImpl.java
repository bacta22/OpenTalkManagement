package com.ncc.asia.service.EmailService;

import com.ncc.asia.entity.CompanyBranch;
import com.ncc.asia.entity.OpenTalk;
import com.ncc.asia.entity.User;
import com.ncc.asia.exception.ItemsNotFoundException;
import com.ncc.asia.repository.OpenTalkRepository;
import com.ncc.asia.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service(value = "emailService")
public class EmailMailServiceImpl implements EmailService {
    @Autowired
    private JavaMailSender emailSender;

    @Autowired
    private SpringTemplateEngine thymeleafTemplateEngine;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OpenTalkRepository openTalkRepository;

    @Override
    public void sendEmailUsingThymeleafTemplate() throws IOException, MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        // Find open talk for this week
        OpenTalk openTalk = openTalkRepository
                .findOpenTalkForSendEmail(LocalDate.now().plusDays(3),"HN");
        if (openTalk == null) {
            throw new ItemsNotFoundException("Could not find any open talk for this week !");
        }

        Set<CompanyBranch> branches = openTalk.getCompanyBranch();
        Set<String> branchesName = branches.stream().map(CompanyBranch::getName).collect(Collectors.toSet());

        // Find users who belong to the branch that takes place open talk, for send email
        List<User> usersForSendEmail = userRepository.findUserForSendEmail(branchesName);
        if (usersForSendEmail == null || usersForSendEmail.isEmpty()) {
            throw new ItemsNotFoundException("Could not find any user who " +
                    "belong to the branch that takes place open talk ");
        }

        helper.setFrom("bactanuce@gmail.com");

        for (User user : usersForSendEmail) {
            Context thymeleafContext = new Context();
            Map<String, Object> templateModel = new HashMap<>();
            templateModel.put("recipientName",user.getFullName());
            templateModel.put("senderName","Admin");
            templateModel.put("meetingLink",openTalk.getLinkMeeting());
            templateModel.put("host",openTalk.getHost().getFullName());

            String dateTime = openTalk.getDate().getDayOfWeek().toString().substring(0,3) + " "
                    + openTalk.getDate().getMonth().toString().substring(0,3) + " "
                    + openTalk.getDate().getDayOfMonth() + " "
                    + openTalk.getDate().getYear() + ", "
                    + "10am - 12pm (ICT)";
            templateModel.put("dateTime",dateTime);

            // subject
            StringBuilder subject = new StringBuilder
                    (String.valueOf("Invitation: Open talk Offline"));
            for (String branchName : branchesName) {
                subject.append(" ").append(branchName);
            }
            subject.append(": ").append(openTalk.getSubject());
            subject.append(" - ").append(openTalk.getHost().getFullName());
            subject.append(" @ ").append(dateTime).append(user.getEmail());
            helper.setSubject(String.valueOf(subject));

            thymeleafContext.setVariables(templateModel);
            String htmlBody = thymeleafTemplateEngine.process("template-thymeleaf.html", thymeleafContext);
            helper.setText(htmlBody,true);
            helper.setTo(user.getEmail());
            emailSender.send(message);
        }
    }


    public void sendEmailWithAttachment() throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");

        String htmlMsg = "<h3>Im testing send a HTML email</h3>"
                + "<img src='https://images.pexels.com/photos/674010/pexels-photo-674010.jpeg?auto=compress&cs=tinysrgb&w=600'>";
        message.setContent(htmlMsg, "text/html");

        FileSystemResource file = new FileSystemResource(new File("test.txt"));
        helper.addAttachment("Demo Mail", file);

        helper.setTo("tadongbac96@gmail.com");
        helper.setSubject("Demo Send Email");

        emailSender.send(message);
    }

}
