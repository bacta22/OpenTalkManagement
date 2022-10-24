package com.ncc.asia.dto.user;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class UserDTOModify {

    private int id;

    private String username;

    private String password;

    private String fullName;

    private LocalDate birthDay;

    private String phoneNumber;

    private boolean enabled;

    // relationship mapping represent one user can have multiple role
    private Set<Integer> rolesId = new HashSet<>();

    // relationship mapping represent one user belong to one company branch
    private Integer companyBranchId;

    // relationship mapping represent one user can join many open talk
    Set<Integer> joinOpenTalkListId = new HashSet<>();

}
