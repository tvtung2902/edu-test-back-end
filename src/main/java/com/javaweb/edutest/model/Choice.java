package com.javaweb.edutest.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;
import java.util.HashSet;

@Entity
@Getter
@Setter
public class Choice extends AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String content;
    private String image;
    private boolean isCorrect;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    private Question question;

    @ManyToMany(mappedBy = "choices")
    private Set<TestHistory> historyOfTest = new HashSet<>();
}
