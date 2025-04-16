package com.javaweb.edutest.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

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
            pageable = PageRequest.of(pageNumber, pageSize);;
        }
        return pageable;
    }
}
