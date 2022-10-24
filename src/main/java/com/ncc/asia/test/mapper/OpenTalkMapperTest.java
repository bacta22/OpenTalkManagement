package com.ncc.asia.test.mapper;

import com.ncc.asia.OpenTalkManagementWebApplication;
import com.ncc.asia.entity.OpenTalk;
import com.ncc.asia.repository.OpenTalkRepository;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class OpenTalkMapperTest {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context
                = new AnnotationConfigApplicationContext(OpenTalkManagementWebApplication.class);
        OpenTalkRepository repo = context.getBean(OpenTalkRepository.class);
        OpenTalk openTalk = repo.findById(1).get();
        System.out.println(openTalk);
    }
}
