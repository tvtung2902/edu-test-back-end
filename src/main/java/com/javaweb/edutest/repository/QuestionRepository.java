package com.javaweb.edutest.repository;

import com.javaweb.edutest.model.Question;
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
    List<Question> findByQuestionTests_Test_Id(Long id);

    @EntityGraph(attributePaths = "categories")
    @Query("""
    SELECT DISTINCT q FROM Question q
    JOIN q.categories c
    WHERE LOWER(q.content) LIKE LOWER(CONCAT('%', :content, '%'))
    AND (:categoryIds IS NULL OR c.id IN :categoryIds)
    ORDER BY q.id DESC
    """)
    Page<Question> getQuestions(String content, List<Long> categoryIds, Pageable pageable);


    @Query("""
    SELECT COUNT(DISTINCT q) FROM Question q
    JOIN q.categories c
    WHERE LOWER(q.content) LIKE LOWER(CONCAT('%', :content, '%'))
    AND (:categoryIds IS NULL OR c.id IN :categoryIds)
""")
    long countByContentAndCategories(@Param("content") String content,
                                     @Param("categoryIds") Collection<Long> categoryIds);

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