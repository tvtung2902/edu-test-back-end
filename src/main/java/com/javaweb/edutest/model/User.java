package com.javaweb.edutest.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
public class User extends AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user")
    private Role role;

    @OneToMany(mappedBy = "owner")
    private Set<Test> tests = new HashSet<>();

    @OneToMany(mappedBy = "author")
    private Set<Comment> comments = new HashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<TestHistory> historyOfTests = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "user_group",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "group_id")
    )
    private Set<Group> ownerGroups = new HashSet<>();

    @OneToMany(mappedBy = "owner")
    private Set<Group> memberGroups = new HashSet<>();
}