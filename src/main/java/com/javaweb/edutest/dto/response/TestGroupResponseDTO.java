package com.javaweb.edutest.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class TestGroupResponseDTO {
    private long id;
    private String name;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private List<ParticipantResponseDTO> users;

    @Getter
    @Setter
    @AllArgsConstructor
     public static class ParticipantResponseDTO {
        private long id;
        private String name;
        private String avatar;
    }
}