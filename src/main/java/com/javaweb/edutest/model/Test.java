package com.javaweb.edutest.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.HashSet;

@Entity
@Getter
@Setter
public class Test extends AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private boolean shuffled;
    private int duration;
    private boolean isPublic;
    private String image;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private User owner;

    @ManyToMany(mappedBy = "tests")
    private Set<Group> groups = new HashSet<>();

    @OneToMany(mappedBy = "test")
    private Set<TestHistory> historyOfTests = new HashSet<>();

    @OneToMany(mappedBy = "test")
    private Set<RootComment> rootComments = new HashSet<>();

    @OneToMany(mappedBy = "test", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private Set<QuestionTest> questionTests;

}