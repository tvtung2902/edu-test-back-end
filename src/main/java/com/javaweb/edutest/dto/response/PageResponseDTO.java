package com.javaweb.edutest.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Builder
public class PageResponseDTO<T> implements Serializable {
    private int pageNo; // page number
    private int pageSize; // item / page
    private int totalPages; // total page
    private T data;
}