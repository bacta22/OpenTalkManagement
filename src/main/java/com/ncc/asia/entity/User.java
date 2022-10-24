package com.ncc.asia.entity;

import com.ncc.asia.dto.opentalk.OpenTalkIsHosted;
import com.ncc.asia.mapper.OpenTalkMapper;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="\"user\"")
@Getter
@Setter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(length = 64, nullable = false, unique = true)
    private String username;

    @Column(length = 64, nullable = false)
    private String password;

    @Column(name = "full_name", length = 64, nullable = false)
    private String fullName;

    private LocalDate birthDay;

    @Column(name = "phone_number", length = 15, nullable = true)
    private String phoneNumber;

    private boolean enabled;

    @Column(length = 45, nullable = true)
    private String email;

    @Column(name = "number_joined_open_talk")
    private int numberOfJoinedOpenTalk;

    // relationship mapping represent one user can have multiple role
    @ManyToMany(fetch = FetchType.EAGER,
                cascade = {CascadeType.DETACH, CascadeType.MERGE,
                            CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
            )
    private Set<Role> roles = new HashSet<>();

    // relationship mapping represent one user belong to one company branch
    @OneToOne(fetch = FetchType.EAGER,
            cascade = {CascadeType.DETACH, CascadeType.MERGE,
                        CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "company_branch_id", referencedColumnName = "id")
    private CompanyBranch companyBranch;

    // relationship mapping represent one user can join many open talk
    @ManyToMany (fetch = FetchType.LAZY,
                cascade = {CascadeType.DETACH, CascadeType.MERGE,
                            CascadeType.PERSIST, CascadeType.REFRESH}, targetEntity = OpenTalk.class)
    @JoinTable(
            name = "user_join_open_talk",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "opentalk_id")
    )
    Set<OpenTalk> joinOpenTalkList = new HashSet<>();

    public User(String username, String password, String fullName, LocalDate birthDay, boolean enabled) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.birthDay = birthDay;
        this.enabled = enabled;
    }

    // relationship mapping represent open talk set is hosted by user
    @OneToMany(fetch = FetchType.LAZY,
            cascade = CascadeType.ALL, mappedBy = "user")
    Set<OpenTalkIsHosted> openTalkIsHosted = new HashSet<>(0);

    // convenience method for add role to user
    public void addRole (Role role) {
        this.roles.add(role);
    }


    // convenience method fod set User hosted open talk,
    // also add this open talk to User's joinOpenTalkList
    public void registerHostOpenTalk (OpenTalk openTalk) {
        this.joinOpenTalkList.add(openTalk);
        openTalk.setHost(this);
        this.numberOfJoinedOpenTalk = this.joinOpenTalkList.size();
    }

    // convenience method for add open talk to joinOpenTalkList
    public void registerJoinedOpenTalk (OpenTalk openTalk) {
        this.joinOpenTalkList.add(openTalk);
        this.numberOfJoinedOpenTalk = this.joinOpenTalkList.size();
    }


    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", fullName='" + fullName + '\'' +
                ", enabled=" + enabled +
                ", companyBranch=" + companyBranch +
                '}';
    }
}


