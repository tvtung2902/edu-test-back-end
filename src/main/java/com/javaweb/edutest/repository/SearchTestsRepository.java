package com.javaweb.edutest.repository;

import com.javaweb.edutest.dto.response.PageResponseDTO;
import com.javaweb.edutest.dto.response.TestGroupResponseDTO;
import com.javaweb.edutest.dto.response.TestResponseDTO1;
import com.javaweb.edutest.enums.TestGroupStatus;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
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

    public PageResponseDTO<TestGroupResponseDTO> getTestsOfGroup(
            long groupId,
            int pageNo,
            int pageSize,
            String searchName,
            TestGroupStatus status
    ) {
        // count
        StringBuilder countSql = new StringBuilder();
        countSql.append("""
        SELECT COUNT(DISTINCT t.id)
        FROM test t
        JOIN group_test gt ON t.id = gt.test_id
        LEFT JOIN test_history th ON th.test_id = t.id
        LEFT JOIN user u ON u.id = th.user_id
        WHERE gt.group_id = :groupId
    """);

        // add condition search
        if (searchName != null && !searchName.isBlank()) {
            countSql.append(" AND LOWER(t.name) LIKE LOWER(CONCAT('%', :searchName, '%'))");
        }

        // add condition status
        if (status != null) {
            countSql.append("""
            AND (
                (:status = 'INCOMING' AND t.start_date > NOW()) OR
                (:status = 'ONGOING' AND t.start_date <= NOW() AND t.end_date >= NOW()) OR
                (:status = 'ENDED' AND t.end_date < NOW())
            )
        """);
        }

        // create query
        Query countQuery = entityManager.createNativeQuery(countSql.toString());
        countQuery.setParameter("groupId", groupId);
        if (searchName != null && !searchName.isBlank()) {
            countQuery.setParameter("searchName", searchName);
        }
        if (status != null) {
            countQuery.setParameter("status", status.name());
        }

        // query
        long totalCount = ((Number) countQuery.getSingleResult()).longValue();

        // total page
        int totalPages = (int) Math.ceil((double) totalCount / pageSize);


        StringBuilder sql = new StringBuilder();
        sql.append("""
        SELECT
            t.id,
            t.image,
            t.name,
            t.start_date,
            t.end_date,
            GROUP_CONCAT(
                 CASE
                     WHEN th.group_id = :groupId THEN CONCAT(u.id, '::', u.username, '::', u.image)
                 END
                 SEPARATOR ';;'
                 ) AS participants
        FROM test t
        JOIN group_test gt ON t.id = gt.test_id
        LEFT JOIN test_history th ON th.test_id = t.id
        LEFT JOIN user u ON u.id = th.user_id
        WHERE gt.group_id = :groupId
    """);

        // add search name
        if (searchName != null && !searchName.isBlank()) {
            sql.append(" AND LOWER(t.name) LIKE LOWER(CONCAT('%', :searchName, '%'))");
        }

        // add search status
        if (status != null) {
            sql.append("""
            AND (
                (:status = 'INCOMING' AND t.start_date > NOW()) OR
                (:status = 'ONGOING' AND t.start_date <= NOW() AND t.end_date >= NOW()) OR
                (:status = 'ENDED' AND t.end_date < NOW())
            )
        """);
        }

        sql.append(" GROUP BY t.id ORDER BY t.start_date DESC LIMIT :limit OFFSET :offset");

        // create query
        Query query = entityManager.createNativeQuery(sql.toString());
        query.setParameter("groupId", groupId);
        query.setParameter("limit", pageSize);
        query.setParameter("offset", pageNo == 0 ? pageSize : (pageNo - 1) * pageSize);

        if (searchName != null && !searchName.isBlank()) {
            query.setParameter("searchName", searchName);
        }

        if (status != null) {
            query.setParameter("status", status.name());
        }

        // query
        List<Object[]> resultList = query.getResultList();

        // mapper to dto
        List<TestGroupResponseDTO> dtoList = resultList.stream().map(row -> {
            long id = ((Number) row[0]).longValue();
            String image = (String) row[2];
            String name = (String) row[1];
            LocalDateTime startDate = ((Timestamp) row[3]).toLocalDateTime();
            LocalDateTime endDate = ((Timestamp) row[4]).toLocalDateTime();
            String rawParticipants = (String) row[5];

            List<TestGroupResponseDTO.ParticipantResponseDTO> users = new ArrayList<>();
            if (rawParticipants != null && !rawParticipants.isEmpty()) {
                String[] parts = rawParticipants.split(";;");
                for (String part : parts) {
                    String[] tokens = part.split("::");
                    if (tokens.length == 3) {
                        long userId = Long.parseLong(tokens[0]);
                        String username = tokens[1];
                        String avatar = tokens[2];
                        users.add(new TestGroupResponseDTO.ParticipantResponseDTO(userId, username, avatar));
                    }
                }
            }

            return new TestGroupResponseDTO(id, image, name, startDate, endDate, users);
        }).toList();

        return PageResponseDTO.<TestGroupResponseDTO>builder()
                .data(dtoList)
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalPages(totalPages)
                .build();
    }


}