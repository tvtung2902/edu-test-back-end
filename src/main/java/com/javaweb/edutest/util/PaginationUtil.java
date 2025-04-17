package com.javaweb.edutest.util;

import com.javaweb.edutest.dto.response.PageResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

public class PaginationUtil {

    public static Pageable createPageable(int pageNo, int pageSize, long totalRecords) {
        int page = 0;
        if (pageNo > 0) {
            page = pageNo - 1;
        }
        Pageable pageable = PageRequest.of(page, pageSize);

        int totalPagesFromDatabase = (int)Math.ceil((double)totalRecords / pageSize);
        int pageNumber = totalPagesFromDatabase > 0 ? (totalPagesFromDatabase - 1) : 0;
        if (pageable.getPageNumber() >= totalPagesFromDatabase) {
            pageable = PageRequest.of(pageNumber, pageSize);
        }
        return pageable;
    }

    public static <T> PageResponseDTO<T> toPageResponse(Page<T> page) {
        return PageResponseDTO.<T>builder()
                .data(page.getContent())
                .pageNo(page.getNumber() + 1)
                .pageSize(page.getSize())
                .totalPages(page.getTotalPages())
                .build();
    }


}
