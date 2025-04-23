package com.javaweb.edutest.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserGroupRequestDTO {
    @NotNull(message = "'users' cannot be null.")
    @NotEmpty(message = "'users' cannot be empty.")
    private List<Long> users;
}
