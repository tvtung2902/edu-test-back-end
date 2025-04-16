package com.javaweb.edutest.repository;

import com.javaweb.edutest.dto.response.CategoryWithQuestionCountDTO;
import com.javaweb.edutest.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query("SELECT c FROM Category c WHERE c.name LIKE %?1% ORDER BY c.id DESC")
    Page<Category> findByNameOrderByIdDesc(String categoryName, Pageable pageable);

    @Query("""
        SELECT c.id AS id, c.name AS name, c.createdAt as createdAt , COUNT(q.id) AS questionCount
        FROM Category c
        LEFT JOIN c.questions q
        WHERE c.name LIKE %?1%
        GROUP BY c.id, c.name, c.createdAt
        ORDER BY c.id DESC
    """)
    Page<CategoryWithQuestionCountDTO> findCategoriesWithQuestionCountDTO(String name, Pageable pageable);

    int countByNameContainingIgnoreCase(String name);
}