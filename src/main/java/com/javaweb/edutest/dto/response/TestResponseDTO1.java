package com.javaweb.edutest.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TestResponseDTO1 {
    private Long id;
    private String name;
    private String description;
    private String image;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Date createdAt;
    private int duration;
    @JsonProperty("isPublic")
    private boolean isPublic;
    private boolean shuffled;
    private Long numberOfQuestion;
}
