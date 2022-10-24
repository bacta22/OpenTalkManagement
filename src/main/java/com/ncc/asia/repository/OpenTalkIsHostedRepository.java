package com.ncc.asia.repository;

import com.ncc.asia.dto.opentalk.OpenTalkIsHosted;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OpenTalkIsHostedRepository extends JpaRepository<OpenTalkIsHosted,Integer> {

    OpenTalkIsHosted findByIdOpenTalk (int idOpenTalk);

}
