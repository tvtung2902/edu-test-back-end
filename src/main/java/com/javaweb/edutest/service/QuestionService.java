package com.javaweb.edutest.service;

import com.javaweb.edutest.dto.request.DeleteQuestionTestDTO;
import com.javaweb.edutest.dto.request.QuestionRequestDTO;
import com.javaweb.edutest.dto.request.QuestionTestRequestDTO;
import com.javaweb.edutest.dto.request.SortQuestionTestDTO;
import com.javaweb.edutest.dto.response.PageResponseDTO;
import com.javaweb.edutest.dto.response.QuestionInTestResponseDTO;
import com.javaweb.edutest.dto.response.QuestionResponseDTO;
import com.javaweb.edutest.model.Question;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface QuestionService {
    PageResponseDTO<QuestionResponseDTO> getQuestions(String content,List<Long> categoryIds,
                                                      int pageNo, int pageSize, Long unassignedTestId);
    PageResponseDTO<QuestionInTestResponseDTO> getQuestionsUnassignedInTest(String testId, String content,List<Long> categoryIds, int pageNo, int pageSize);
    QuestionResponseDTO getQuestionById(long questionId);
    List<QuestionInTestResponseDTO> getQuestionsInTest(long testId);
    Question addQuestion(QuestionRequestDTO questionRequestDTO,
                         MultipartFile image, List<MultipartFile> imageAnswers) throws IOException;
    long addQuestionToTest(long testId, QuestionRequestDTO questionRequestDTO,
                           MultipartFile image, List<MultipartFile> imageAnswers);
    void addQuestionFromLibraryToTest(long testId, QuestionTestRequestDTO request);
    void updateQuestion(long questionId, QuestionRequestDTO questionRequestDTO,
                        MultipartFile image, List<MultipartFile> imageAnswerFiles);
    void updateCategoriesOfQuestion(long questionId, List<Long> newCategoryIds);
    void deleteQuestion(long questionId);
    void deleteQuestionFromTest(long testId, DeleteQuestionTestDTO request);

    void sortQuestionsInTest(long testId, List<SortQuestionTestDTO> request);
}