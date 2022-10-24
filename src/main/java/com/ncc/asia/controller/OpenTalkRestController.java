package com.ncc.asia.controller;

import com.ncc.asia.dto.opentalk.OpenTalkDTO;
import com.ncc.asia.service.OpenTalkService.OpenTalkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/opentalks")
public class OpenTalkRestController {

    @Autowired
    private OpenTalkService service;

    // CRUD
    @GetMapping
    public ResponseEntity<List<OpenTalkDTO>> findAll () {
        return ResponseEntity.ok().body(service.findAll());
    }

    @GetMapping("/{openTalkId}")
    public ResponseEntity<OpenTalkDTO> findById (@PathVariable("openTalkId") int id) {
        return ResponseEntity.ok().body(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<OpenTalkDTO> create (@RequestBody OpenTalkDTO openTalkDTO) {
        openTalkDTO.setId(0);
        OpenTalkDTO savedOpenTalk = service.save(openTalkDTO);
        return new ResponseEntity<OpenTalkDTO>(savedOpenTalk, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<OpenTalkDTO> update (@RequestBody OpenTalkDTO openTalkDTO) {
        OpenTalkDTO savedOpenTalk = service.save(openTalkDTO);
        return new ResponseEntity<OpenTalkDTO>(savedOpenTalk,HttpStatus.OK);
    }

    @DeleteMapping("/{openTalkId}")
    public ResponseEntity<Void> deleteById (@PathVariable("openTalkId") int id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // Find open talk already finished
    @GetMapping("/finished")
    public ResponseEntity<Page<OpenTalkDTO>> findAlreadyFinishedOpenTalk (
            @RequestParam(required = false) String branch,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "3") Integer pageSize
            ) {
        Page<OpenTalkDTO> openTalkDTOPage
                = service.findByAlreadyFinishedOpenTalk(branch,username,LocalDateTime.now(),
                                                            startDate,endDate,pageNo,pageSize);
        return ResponseEntity.ok().body(openTalkDTOPage);
    }

    // Find open talk is coming
    @GetMapping("/comingTest")
    public ResponseEntity<Page<OpenTalkDTO>> findIsComingOpenTalkTest (
            @RequestParam(required = false) String branch,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "3") Integer pageSize
    ) {
        Page<OpenTalkDTO> openTalkDTOPage
                = service.findByIsComingOpenTalk(branch,username,LocalDateTime.now(),
                                                        LocalDateTime.now().plusMonths(1),
                                                        startDate,endDate,pageNo,pageSize);
        return ResponseEntity.ok().body(openTalkDTOPage);
    }


    // Find open talk by user id
    @GetMapping("/user/{id}")
    public ResponseEntity<Page<OpenTalkDTO>> findByUserId (@PathVariable("id") Integer id,
                                                           @RequestParam(defaultValue = "0") Integer pageNo,
                                                           @RequestParam(defaultValue = "3") Integer pageSize,
                                                           @RequestParam(required = false)
                                                               @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                                               LocalDate startDate,
                                                           @RequestParam(required = false)
                                                               @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                                               LocalDate endDate) {
        Page<OpenTalkDTO> openTalkDTOPage = service.findByUserId(id,pageNo,pageSize,startDate,endDate);
        return new ResponseEntity<>(openTalkDTOPage,HttpStatus.OK);
    }

    // Find open talk already joined for user, sort by register date desc
    @GetMapping("/user/{id}/sortByDate")
    public ResponseEntity<Page<OpenTalkDTO>> findByAlreadyJoinedOpenTalkForUser
                                                            (@PathVariable("id") Integer id,
                                                             @RequestParam(defaultValue = "0") Integer pageNo,
                                                             @RequestParam(defaultValue = "3") Integer pageSize) {
        Page<OpenTalkDTO> openTalkDTOPage = service.findByAlreadyJoinedOpenTalkForUser(id,pageNo,pageSize);
        return new ResponseEntity<>(openTalkDTOPage,HttpStatus.OK);
    }
}
