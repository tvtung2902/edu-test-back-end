package com.javaweb.edutest.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChoiceResponseDTO {
    private long id;
    private String content;
    private String image;
    @JsonProperty("isCorrect")
    private boolean isCorrect;
}