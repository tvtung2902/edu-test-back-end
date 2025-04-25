package com.javaweb.edutest.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public interface GroupResponseDTOWithCount {
    Long getId();
    String getName();
    String getDescription();
    String getCode();
    String getImage();
    @JsonProperty("numberOfMembers")
    int getMemberCount();
    @JsonProperty("numberOfTests")
    int getTestsCount();
    Date getCreatedAt();
}