package com.javaweb.edutest.dto.response;

import java.time.LocalDateTime;

public interface TestResponseDTO {
    String getId();
    String getName();
    String getDescription();
    String getImage();
    LocalDateTime getStartDate();
    LocalDateTime getEndDate();
    LocalDateTime getCreatedAt();
    Integer getDuration();
    boolean isPublic();
    boolean isShuffled();
    int getNumberOfQuestion();
}