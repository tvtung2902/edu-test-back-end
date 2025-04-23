package com.javaweb.edutest.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TestGroupRequestDTO {
    @NotNull(message = "'testIds' cannot be null.")
    @NotEmpty(message = "'testIds' cannot be empty.")
    private List<Long> testIds;
}
