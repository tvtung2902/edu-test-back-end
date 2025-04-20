package com.javaweb.edutest.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChoiceRequestDTO {
    private Long id;
    private String content;
    @JsonProperty("isCorrect")
    private boolean isCorrect;
    private boolean changedImg;
    private boolean add;
}
