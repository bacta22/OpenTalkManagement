package com.ncc.asia.entity;

import lombok.*;

import javax.persistence.*;

@Entity(name = "Role")
@Table
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false,unique = true)
    private String name;

    public Role(int id) {
        this.id = id;
    }
}
