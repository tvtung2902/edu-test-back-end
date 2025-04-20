package com.javaweb.edutest.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Date;
import java.util.List;

import java.util.Set;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
public class QuestionResponseDTO {
    private long id;
    private String content;
    private String explanation;
    private String image;
    Set<CategoryResponseDTO> categories;
    @JsonProperty("options")
    List<ChoiceResponseDTO> choices;
    private Date createdAt;
}