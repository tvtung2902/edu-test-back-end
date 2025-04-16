package com.javaweb.edutest.model;

import com.javaweb.edutest.model.compositekey.QuestionTestPK;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "question_test")
public class QuestionTest extends AbstractEntity {
    @EmbeddedId
    private QuestionTestPK id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("questionId")
    @JoinColumn(name = "question_id")
    private Question question;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("testId")
    @JoinColumn(name = "test_id")
    private Test test;

    private int orderNumber;
}