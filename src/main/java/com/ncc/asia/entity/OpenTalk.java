package com.ncc.asia.entity;

import com.fasterxml.jackson.annotation.*;
import com.ncc.asia.mapper.OpenTalkMapper;
import com.ncc.asia.mapper.OpenTalkMapperImpl;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class OpenTalk {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(length = 64, nullable = false)
    private String subject;

    private LocalDateTime date;

    @Column(name = "link_meeting", nullable = false)
    private String linkMeeting;

    // many user can host one open talk ???
    // relationship mapping represent one user host open talk
    @OneToOne(fetch = FetchType.EAGER,
            cascade = {CascadeType.DETACH, CascadeType.MERGE,
            CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User host;

    // relationship mapping represent one open talk may be for many company branch
    @ManyToMany(fetch = FetchType.EAGER,
            cascade = {CascadeType.DETACH, CascadeType.MERGE,
                    CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(
            name = "branch_opentalk",
            joinColumns = @JoinColumn(name = "open_talk_id"),
            inverseJoinColumns = @JoinColumn(name = "branch_id")
    )
    private Set<CompanyBranch> companyBranch = new HashSet<>();

    // relationship mapping represent one open talk can have many users
    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {CascadeType.DETACH, CascadeType.MERGE,
                    CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(
            name = "user_join_open_talk",
            joinColumns = @JoinColumn(name = "opentalk_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> users = new HashSet<>();

    public OpenTalk(int id) {
        this.id = id;
    }

    public OpenTalk(String subject, LocalDateTime date, String linkMeeting) {
        this.subject = subject;
        this.date = date;
        this.linkMeeting = linkMeeting;
    }

    // convenience method for set User hosted open talk,
    // also add this open talk to User's joinOpenTalkList
    public void setHostUser(User user) {
        setHost(user);
        this.users.add(user);
    }

    public void setUserJoinedOpenTalk (Set<User> users) {
        this.setUsers(users);
    }

    public void registerOpenTalkForUser(User user) {
        this.users.add(user);
    }

    // convenience method for add company branch for open talk
    public void addCompanyBranch (CompanyBranch branch) {
        this.companyBranch.add(branch);
    }

    @Override
    public String toString() {
        return "OpenTalk{" +
                "id=" + id +
                ", subject='" + subject + '\'' +
                ", date=" + date +
                ", linkMeeting='" + linkMeeting + '\'' +
                '}';
    }
}



/*
@JsonIdentityInfo : Annotation used for indicating that values of annotated type or property
should be serializing so that instances either contain additional object identifier
(in addition actual object properties), or as a reference that consists of an object id that
refers to a full serialization. In practice this is done by serializing the first instance as full
object and object identity, and other references to the object as reference values.

@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")

        + generator = ObjectIdGenerators.PropertyGenerator.class :
         Generator to use for producing Object Identifier for objects

        + property = "id": OpenTalk's property, based on annotated property to get the Open talk object independent
        So this property must unique, simply choose id field
        + In case if we choose property have same values => error
 */