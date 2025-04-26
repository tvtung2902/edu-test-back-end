package com.javaweb.edutest.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Getter
@Setter
public class QuestionInTestResponseDTO extends QuestionResponseDTO{
    private int orderNumber;

    public QuestionInTestResponseDTO(long id, String content, String explanation, String image, Set<CategoryResponseDTO> categories, List<ChoiceResponseDTO> choices, Date createdAt, int orderNumber) {
        super(id, content, explanation, image, categories, choices, createdAt);
        this.orderNumber = orderNumber;
    }
}
