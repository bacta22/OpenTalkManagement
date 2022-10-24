package com.ncc.asia.mapper;

import com.ncc.asia.dto.opentalk.OpenTalkDTO;
import com.ncc.asia.entity.OpenTalk;
import com.ncc.asia.repository.OpenTalkRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class OpenTalkMapperTests {

    @Autowired
    private OpenTalkMapper openTalkMapper;

    @Autowired
    private OpenTalkRepository openTalkRepository;

    @Test
    public void testEntityToDto () {
        OpenTalk openTalk = openTalkRepository.findById(1).get();
        OpenTalkDTO openTalkDTO = openTalkMapper.entityToDto(openTalk);
        System.out.println(openTalk);
        System.out.println(openTalkDTO);

    }
}
