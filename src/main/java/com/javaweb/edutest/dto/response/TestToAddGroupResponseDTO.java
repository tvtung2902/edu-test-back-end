package com.javaweb.edutest.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class TestToAddGroupResponseDTO {
    long id;
    String name;
    String description;
    int duration;
}
