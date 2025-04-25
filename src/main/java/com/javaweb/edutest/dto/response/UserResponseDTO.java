package com.javaweb.edutest.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserResponseDTO {
    private Long id;
    private String username;
    private String name;
    private String email;
    @JsonProperty("avatar")
    private String image;
}