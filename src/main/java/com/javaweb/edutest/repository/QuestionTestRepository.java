package com.javaweb.edutest.repository;

import com.javaweb.edutest.model.QuestionTest;
import com.javaweb.edutest.model.compositekey.QuestionTestPK;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface QuestionTestRepository extends JpaRepository<QuestionTest, QuestionTestPK> {
    Optional<QuestionTest> findTopByTestIdOrderByOrderNumberDesc(long testId);
    List<QuestionTest> findByTestIdAndOrderNumberLessThanEqual(long testId, int orderNumber);
    List<QuestionTest> findByTestId(long testId);
}