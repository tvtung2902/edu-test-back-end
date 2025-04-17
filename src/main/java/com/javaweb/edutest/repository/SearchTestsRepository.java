package com.javaweb.edutest.repository;

import ch.qos.logback.core.util.StringUtil;
import com.javaweb.edutest.dto.response.PageResponseDTO;
import com.javaweb.edutest.dto.response.TestResponseDTO1;
import com.javaweb.edutest.util.PaginationUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

@Repository
public class SearchTestsRepository {
    @PersistenceContext
    private EntityManager entityManager;
    public Page<TestResponseDTO1> findTests(String searchName, Boolean isPublic, Pageable pageable, long totalRecords){
        String baseQuery = """
            SELECT new com.javaweb.edutest.dto.response.TestResponseDTO1(
                t.id, t.name, t.description, t.image, t.startDate, t.endDate, t.createdAt,
                t.duration, t.isPublic, t.shuffled, COUNT(qt)
            )
            FROM Test t
            LEFT JOIN t.questionTests qt
        """;

        StringBuilder whereClause = new StringBuilder(" WHERE 1 = 1 ");

        if(StringUtils.hasText(searchName)){
            whereClause.append(" AND LOWER(t.name) LIKE LOWER(CONCAT('%', :searchName, '%'))");
        }

        if (isPublic != null) {
            whereClause.append(" AND t.isPublic = :isPublic ");
        }

        String groupOrderClause = """
        GROUP BY t.id, t.name, t.description, t.image, t.startDate, t.endDate,
                 t.createdAt, t.duration, t.isPublic, t.shuffled
        ORDER BY t.id DESC
    """;

        String finalQuery = baseQuery + whereClause + groupOrderClause;
        Query query = entityManager.createQuery(finalQuery, TestResponseDTO1.class);

        if (StringUtils.hasText(searchName)) {
            query.setParameter("searchName", searchName);
        }

        if (isPublic != null) {
            query.setParameter("isPublic", isPublic);
        }

        query.setFirstResult(pageable.getPageNumber());
        query.setMaxResults(pageable.getPageSize());

        var tests = query.getResultList();

        return new PageImpl<>(tests, pageable, totalRecords);
    }
}
