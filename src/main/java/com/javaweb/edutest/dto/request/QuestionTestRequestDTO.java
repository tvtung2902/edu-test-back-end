package com.javaweb.edutest.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class QuestionTestRequestDTO {
    @NotNull(message = "'questionIds' cannot be null.")
    @NotEmpty(message = "'questionIds' cannot be empty.")
    private List<Long> questionIds;
}
