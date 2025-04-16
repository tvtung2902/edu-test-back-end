package com.javaweb.edutest.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
public class Question extends AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    private String explanation;

    private String image;

    @ManyToMany
    @JoinTable(name = "question_category",
            joinColumns = @JoinColumn(name = "question_id", nullable = false),
            inverseJoinColumns = @JoinColumn (name = "category_id", nullable = false))
    private Set<Category> categories = new HashSet<>();

    @OneToMany(mappedBy = "question", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private Set<Choice> choices = new HashSet<>();

    @OneToMany(mappedBy = "question", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private Set<QuestionTest> questionTests = new HashSet<>();
}
