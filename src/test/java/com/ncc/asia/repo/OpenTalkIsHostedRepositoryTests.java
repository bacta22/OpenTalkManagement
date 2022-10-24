package com.ncc.asia.repo;

import com.ncc.asia.dto.opentalk.OpenTalkIsHosted;
import com.ncc.asia.repository.OpenTalkIsHostedRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class OpenTalkIsHostedRepositoryTests {

    @Autowired
    private OpenTalkIsHostedRepository openTalkIsHostedRepository;

    @Test
    public void testGetOpenTalkIsHostedByIdOpenTalk () {
        OpenTalkIsHosted openTalkIsHosted = openTalkIsHostedRepository.findByIdOpenTalk(12);
        System.out.println(openTalkIsHosted.getSubject());
    }
}
