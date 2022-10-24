package com.ncc.asia.test;

import com.ncc.asia.dto.user.UserDTOModify;
import com.ncc.asia.service.EmailService.EmailMailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @Autowired
    EmailMailServiceImpl emailMailService;



    @GetMapping("/userModify")
    public ResponseEntity<UserDTOModify> test () {

        UserDTOModify accountUserDTO = new UserDTOModify();
        accountUserDTO.setUsername("bac");
        accountUserDTO.setPassword("bac");
        accountUserDTO.setFullName("bac full");
        accountUserDTO.setBirthDay(LocalDate.now());
        accountUserDTO.setPhoneNumber("123456");
        accountUserDTO.setEnabled(true);

        Set<Integer> rolesId = new HashSet<>();
        rolesId.add(1);rolesId.add(2);
        accountUserDTO.setRolesId(rolesId);

        accountUserDTO.setCompanyBranchId(3);

        Set<Integer> openTalkListId = new HashSet<>();
        openTalkListId.add(4);
        openTalkListId.add(5);
        accountUserDTO.setJoinOpenTalkListId(openTalkListId);

        return ResponseEntity.ok().body(accountUserDTO);
    }


    @GetMapping("/sendmail")
    public String sendEmail() throws MessagingException {
        emailMailService.sendEmailWithAttachment();
        return "OK!!!!!!";
    }

//    @Autowired
//    private UserTestRepository userTestRepository;
//
//    @GetMapping("/findByYear")
//    public ResponseEntity<List<UserTest>> getByYear (@RequestParam("year") int year) {
//
//        return ResponseEntity.ok().body(userTestRepository.findByYear(year));
//    }
}
