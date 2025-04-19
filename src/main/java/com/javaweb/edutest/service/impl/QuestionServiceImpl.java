package com.javaweb.edutest.service.impl;

import com.javaweb.edutest.dto.request.QuestionRequestDTO;
import com.javaweb.edutest.dto.response.PageResponseDTO;
import com.javaweb.edutest.dto.response.QuestionResponseDTO;
import com.javaweb.edutest.exception.ResourceNotFoundException;
import com.javaweb.edutest.mapper.QuestionMapper;
import com.javaweb.edutest.model.Category;
import com.javaweb.edutest.model.Choice;
import com.javaweb.edutest.model.Question;
import com.javaweb.edutest.model.QuestionTest;
import com.javaweb.edutest.model.compositekey.QuestionTestPK;
import com.javaweb.edutest.repository.CategoryRepository;
import com.javaweb.edutest.repository.QuestionRepository;
import com.javaweb.edutest.repository.TestRepository;
import com.javaweb.edutest.service.CloudinaryService;
import com.javaweb.edutest.service.QuestionService;
import com.javaweb.edutest.util.PaginationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashSet;
import java.util.*;
import java.util.stream.Collectors;

@Primary
@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {

    private final QuestionMapper questionMapper;
    private final QuestionRepository questionRepository;
    private final CategoryRepository categoryRepository;
    private final TestRepository testRepository;
    private final CloudinaryService cloudinaryService;

    @Override
    public PageResponseDTO<QuestionResponseDTO> getQuestions(String content, List<Long> categoryIds, int pageNo, int pageSize) {
        long totalRecords = questionRepository.countByContentAndCategories(content, categoryIds);
        Pageable page = PaginationUtil.createPageable(pageNo, pageSize, totalRecords);
        Page<Question> questions = questionRepository.getQuestions(content, categoryIds, page);
        Page<QuestionResponseDTO> questionResponseDTOs = questions.map(questionMapper::toQuestionResponseDTO);
        return PaginationUtil.toPageResponse(questionResponseDTOs);
    }

    @Override
    public QuestionResponseDTO getQuestionById(long questionId) {
        return questionMapper.toQuestionResponseDTO(questionRepository.getQuestionById(questionId));
    }

    @Override
    public List<QuestionResponseDTO> getQuestionsInTest(long testId) {
        return questionMapper.toQuestionResponseDTOs(questionRepository.findByQuestionTests_Test_Id(testId));
    }

    @Override
    @Transactional
    public long addQuestion(QuestionRequestDTO questionRequestDTO, MultipartFile image, List<MultipartFile> imageAnswerFiles) throws IOException {
        long newQuestionId;
        String imageUrl = null;
        List<String> imageAnswers = new ArrayList<>();
        try{
            imageUrl = cloudinaryService.uploadFile(image);
            imageAnswerFiles.forEach(imageAnswerFile -> {
                if(imageAnswerFile == null || imageAnswerFile.isEmpty() || imageAnswerFile.getSize() == 0 ){
                    imageAnswers.add(null);
                }
                else{
                    try {
                        String imageAnswerUrl = cloudinaryService.uploadFile(imageAnswerFile);
                        imageAnswers.add(imageAnswerUrl);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        } finally{
            newQuestionId = addQuestionToDB(questionRequestDTO, imageUrl, imageAnswers).getId();
        }
        return newQuestionId;
    }

    @Override
    @Transactional
    public long addQuestionToTest(long testId, QuestionRequestDTO questionRequestDTO) {
//        var newQuestion = addQuestionToDB(questionRequestDTO);
//        var test = testRepository.findById(testId).orElseThrow(
//                () -> new ResourceNotFoundException("test not found with id " +testId)
//        );
//        QuestionTest questionTest = QuestionTest.builder()
//                .id(QuestionTestPK.builder()
//                        .questionId(newQuestion.getId())
//                        .testId(test.getId())
//                        .build())
//                .question(newQuestion)
//                .test(test)
//                .build();
//        newQuestion.getQuestionTests().add(questionTest);
//        questionRepository.save(newQuestion);
//        return newQuestion.getId();
        return -1;
    }

    @Override
    public void addQuestionFromLibraryToTest(long testId, Map<String, List<Long>> request) {
        var test = testRepository.findById(testId).orElseThrow(
                () -> new ResourceNotFoundException("test not found with id " +testId)
        );
        List<Long> questionIds = request.get("questionIds");
        List<Question> questions = new ArrayList<>();
        questionIds.forEach(questionId -> {
            Question question = findQuestionById(questionId);
            QuestionTest questionTest = QuestionTest.builder()
                    .id(QuestionTestPK.builder()
                            .questionId(questionId)
                            .testId(test.getId())
                            .build())
                    .test(test)
                    .question(findQuestionById(questionId))
                    .build();
            question.getQuestionTests().add(questionTest);
            questions.add(findQuestionById(questionId));
        });

        questionRepository.saveAll(questions);
    }

    @Override
    public void updateQuestion(long questionId, QuestionRequestDTO questionRequestDTO) {
        Question currentQuestion = findQuestionById(questionId);
        questionMapper.toQuestion(currentQuestion, questionRequestDTO);
        updateCategoriesToQuestion(questionRequestDTO.getCategoryIds(), currentQuestion);
        setQuestionToChoices(currentQuestion);
        questionRepository.save(currentQuestion);
    }

    @Override
    public void updateCategoriesOfQuestion(long questionId, List<Long> newCategoryIds) {
        Question currentQuestion = findQuestionById(questionId);
        currentQuestion.setCategories(fetchCategoryByIds(newCategoryIds));
        questionRepository.save(currentQuestion);
    }

    @Override
    public void deleteQuestion(long questionId) {
        Question question = questionRepository.getQuestionsAndChoicesByQuestionId(questionId);
        try {
            cloudinaryService.deleteFile(question.getImage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Set<Choice> choices = question.getChoices();
        choices.forEach(choice -> {
            try {
                cloudinaryService.deleteFile(choice.getImage());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        questionRepository.deleteById(questionId);

    }

    private void addCategoriesToQuestion(List<Long> categoryIds, Question question) {
        if(categoryIds != null && !categoryIds.isEmpty()){
            var categories = fetchCategoryByIds(categoryIds);
            question.getCategories().addAll(categories);
        }
    }

    private void updateCategoriesToQuestion(List<Long> categoryIds, Question question) {
        if(categoryIds != null && !categoryIds.isEmpty()){
            var categories = fetchCategoryByIds(categoryIds);
            question.setCategories(categories);
        }
        else {
            question.getCategories().clear();
        }
    }

    private void setQuestionToChoices(Question question) {
        question.getChoices().forEach(choice -> choice.setQuestion(question));
    }

    private Set<Category> fetchCategoryByIds(List<Long> categoryIds) {
        var categories = new HashSet<Category>();
        categoryIds.forEach(categoryId -> categories.add(findCategoryById(categoryId)));
        return categories;
    }

    private Question findQuestionById(long questionId){
        return questionRepository.findById(questionId).orElseThrow(
                () -> new ResourceNotFoundException("question not found with id " + questionId)
        );
    }

    private Category findCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId).orElseThrow(
                () -> new ResourceNotFoundException("Category not found with id " + categoryId));
    }

    private Question addQuestionToDB(QuestionRequestDTO questionRequestDTO, String imgQuestionUrl, List<String> imgAnswersUrl) {
        Question newQuestion = questionMapper.toQuestion(questionRequestDTO);
        newQuestion.setImage(imgQuestionUrl);

        final int[] i = {0};

        newQuestion.setChoices(newQuestion.getChoices().stream().map(choice -> {
            choice.setImage(imgAnswersUrl.get(i[0]++));
            return choice;
        }).collect(Collectors.toSet()));

        addCategoriesToQuestion(questionRequestDTO.getCategoryIds(), newQuestion);
        setQuestionToChoices(newQuestion);

        return questionRepository.save(newQuestion);
    }
}