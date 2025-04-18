package com.javaweb.edutest.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public interface GroupResponseDTOWithCount {
    Long getId();
    String getName();
    String getDescription();
    String getCode();
    String getImage();
    @JsonProperty("numberOfTests")
    int getMemberCount();
    @JsonProperty("numberOfMembers")
    int getTestsCount();
    Date getCreatedAt();
}