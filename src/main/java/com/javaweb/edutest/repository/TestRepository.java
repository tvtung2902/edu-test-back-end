package com.javaweb.edutest.repository;

import com.javaweb.edutest.dto.response.TestResponseDTO;
import com.javaweb.edutest.dto.response.TestToAddGroupResponseDTO;
import com.javaweb.edutest.model.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface TestRepository extends JpaRepository<Test, Long> {

    int countByNameContainingIgnoreCase(String name);

    @Query("SELECT COUNT(t) FROM Test t WHERE LOWER(t.name) LIKE LOWER(CONCAT('%', :name, '%')) AND t.isPublic = :isPublic")
    int countTests(@Param("name") String name, @Param("isPublic") boolean isPublic);

    @Query("""
            SELECT t.id as id, t.name as name , t.description as description, t.createdAt as createdAt,
            t.startDate as startDate, t.endDate as endDate, t.shuffled as shuffled, t.duration as duration, 
            t.isPublic as public, t.image as image, COUNT(qt) as numberOfQuestion
            FROM Test t LEFT JOIN t.questionTests qt
            WHERE t.name LIKE %?1%
            GROUP BY t.id, t.name, t.description, t.startDate, t.endDate, t.shuffled, t.duration, t.isPublic, t.image
            ORDER BY t.id DESC
            """)
    Page<TestResponseDTO> findTests(String searchName, Pageable pageable);

    @Query("""
    SELECT t FROM Test t
    WHERE t.owner.id = :userId
    AND t.id NOT IN (
        SELECT test.id FROM Group g
        JOIN g.tests test
        WHERE g.id = :groupId
    )
""")
    List<Test> findTestsByUserIdNotInGroup(@Param("userId") Long userId,
                                                                @Param("groupId") Long groupId);


}