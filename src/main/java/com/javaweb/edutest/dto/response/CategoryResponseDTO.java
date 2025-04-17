package com.javaweb.edutest.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class CategoryResponseDTO {
    private long id;
    private String name;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Set<QuestionResponseDTO> questions;
    private int numberOfQuestions;
}