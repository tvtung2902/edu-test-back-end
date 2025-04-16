package com.javaweb.edutest.dto.response;

public interface CategoryWithQuestionCountDTO {
    Long getId();
    String getName();
    Long getQuestionCount();
    String getCreatedAt();
}