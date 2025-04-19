package com.javaweb.edutest.service;

import com.javaweb.edutest.dto.request.QuestionRequestDTO;
import com.javaweb.edutest.dto.response.PageResponseDTO;
import com.javaweb.edutest.dto.response.QuestionResponseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface QuestionService {
    PageResponseDTO<QuestionResponseDTO> getQuestions(String content,List<Long> categoryIds, int pageNo, int pageSize);
    QuestionResponseDTO getQuestionById(long questionId);
    List<QuestionResponseDTO> getQuestionsInTest(long testId);
    long addQuestion(QuestionRequestDTO questionRequestDTO, MultipartFile image, List<MultipartFile> imageAnswers) throws IOException;
    long addQuestionToTest(long testId, QuestionRequestDTO questionRequestDTO);
    void addQuestionFromLibraryToTest(long testId, Map<String, List<Long>> request);
    void updateQuestion(long questionId, QuestionRequestDTO questionRequestDTO);
    void updateCategoriesOfQuestion(long questionId, List<Long> newCategoryIds);
    void deleteQuestion(long questionId);
}
