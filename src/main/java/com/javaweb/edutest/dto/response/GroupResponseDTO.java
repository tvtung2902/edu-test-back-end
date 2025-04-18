package com.javaweb.edutest.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class GroupResponseDTO {
    private Long id;
    private String name;
    private String description;
    private String code;
    private String image;
    private Date createdAt;
}