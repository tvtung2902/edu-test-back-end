package com.javaweb.edutest.repository;

import com.javaweb.edutest.model.Question;
import com.javaweb.edutest.model.QuestionTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    @EntityGraph(attributePaths = {"categories", "choices"})
    @Query("""
           SELECT qt
           FROM QuestionTest qt
           WHERE qt.test.id = :id
           ORDER BY qt.orderNumber ASC
           """)
    List<QuestionTest> findByQuestionTests_Test_IdOrderByQuestionTests_OrderNumberAsc(@Param("id") Long id);

    @EntityGraph(attributePaths = "categories")
    @Query("""
    SELECT DISTINCT q FROM Question q
    JOIN q.categories c
    WHERE LOWER(q.content) LIKE LOWER(CONCAT('%', :content, '%'))
    AND (:categoryIds IS NULL OR c.id IN :categoryIds)
    AND (
        :unassignedTestId IS NULL OR NOT EXISTS (
            SELECT 1 FROM QuestionTest qt
            WHERE qt.question = q AND qt.test.id = :unassignedTestId
        )
    )
    ORDER BY q.id DESC
""")
    Page<Question> getQuestions(String content, List<Long> categoryIds, Long unassignedTestId, Pageable pageable);


    @Query("""
    SELECT COUNT(DISTINCT q) FROM Question q
    JOIN q.categories c
    WHERE LOWER(q.content) LIKE LOWER(CONCAT('%', :content, '%'))
    AND (:categoryIds IS NULL OR c.id IN :categoryIds)
            AND (
            :unassignedTestId IS NULL OR NOT EXISTS (
                SELECT 1 FROM QuestionTest qt
                WHERE qt.question = q AND qt.test.id = :unassignedTestId
            )
        )
    
    """)
    long countByContentAndCategories(@Param("content") String content,
                                     @Param("categoryIds") Collection<Long> categoryIds,
                                     @Param("unassignedTestId") Long unassignedTestId);

    @Query("""
    SELECT DISTINCT q FROM Question q
    JOIN FETCH q.choices c
    WHERE q.id = :questionId
""")
    Question getQuestionsAndChoicesByQuestionId(Long questionId);

    @Query("""
    SELECT q FROM Question q
    LEFT JOIN FETCH q.choices
    LEFT JOIN FETCH q.categories
    WHERE q.id = :questionId
    """)
    Optional<Question> getQuestionById(long questionId);
}