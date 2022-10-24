package com.ncc.asia.test.managehost;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

//@Entity
@Getter
@Setter
@NoArgsConstructor
public class UserTest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    @OneToMany
    private Set<OpenTalkIsHostedTest> openTalkIsHosted = new HashSet<>();
}
