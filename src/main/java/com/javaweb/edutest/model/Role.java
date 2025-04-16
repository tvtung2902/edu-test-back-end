package com.javaweb.edutest.model;

import com.javaweb.edutest.enums.UserStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
public class Role extends AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private UserStatus userStatus;

    @OneToMany(mappedBy = "role")
    private Set<User> users = new HashSet<>();
}