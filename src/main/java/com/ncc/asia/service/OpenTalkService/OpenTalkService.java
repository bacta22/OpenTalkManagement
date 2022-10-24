package com.ncc.asia.service.OpenTalkService;

import com.ncc.asia.dto.opentalk.OpenTalkDTO;
import org.springframework.data.domain.Page;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface OpenTalkService {

    public OpenTalkDTO findById(int id);

    public List<OpenTalkDTO> findAll ();

    public OpenTalkDTO save (OpenTalkDTO openTalkDTO);

    public void deleteById (int id);

    // Get open talk already finished
    public Page<OpenTalkDTO> findByAlreadyFinishedOpenTalk (String branch, String username, LocalDateTime now,
                                                                LocalDate startDate, LocalDate endDate,
                                                                Integer pageNo, Integer pageSize);

    // Get open talk is coming
    public Page<OpenTalkDTO> findByIsComingOpenTalk (String branch, String username, LocalDateTime now,
                                                                LocalDateTime estimateDate,
                                                                LocalDate startDate, LocalDate endDate,
                                                                Integer pageNo, Integer pageSize);

    // Find open talk by user id
    public Page<OpenTalkDTO> findByUserId (Integer id, Integer pageNo, Integer pageSize,
                                          LocalDate startDate, LocalDate endDate);

    // Find open talk already joined for user, sort by register date desc
    Page<OpenTalkDTO> findByAlreadyJoinedOpenTalkForUser (Integer id,
                                                          Integer pageNo, Integer pageSize);

}
