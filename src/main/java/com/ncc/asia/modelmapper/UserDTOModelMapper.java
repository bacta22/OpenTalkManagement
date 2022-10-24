package com.ncc.asia.modelmapper;

import com.ncc.asia.entity.Role;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Set;

@Getter
@Setter
@ToString
public class UserDTOModelMapper {
    private int id;
    private String username;
    private String fullName;
    private String phoneNumber;
    private Set<Role> roles;

}
