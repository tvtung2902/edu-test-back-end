package com.javaweb.edutest.model.compositekey;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionTestPK {
    private long testId;
    private long questionId;
}