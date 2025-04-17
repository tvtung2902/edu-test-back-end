package com.javaweb.edutest.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;
import java.util.HashSet;

@Entity
@Getter
@Setter
@Table(name = "class")
public class Group extends AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private String code;

    @ManyToMany(mappedBy = "ownerGroups")
    private Set<User> members = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private User owner;

    @ManyToMany
    @JoinTable(
            name = "group_test",
            joinColumns = @JoinColumn(name = "group_id"),
            inverseJoinColumns = @JoinColumn(name = "test_id")
    )
    private Set<Test> tests = new HashSet<>();
}