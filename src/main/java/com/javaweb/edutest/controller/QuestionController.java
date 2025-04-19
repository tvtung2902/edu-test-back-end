package com.javaweb.edutest.controller;

import com.javaweb.edutest.dto.request.GroupRequestDTO;
import com.javaweb.edutest.dto.request.QuestionRequestDTO;
import com.javaweb.edutest.dto.response.PageResponseDTO;
import com.javaweb.edutest.dto.response.QuestionResponseDTO;
import com.javaweb.edutest.dto.response.ResponseData;
import com.javaweb.edutest.exception.ResourceNotFoundException;
import com.javaweb.edutest.service.QuestionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/questions")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "http://localhost:3000")
public class QuestionController {

    private final QuestionService questionService;

    @GetMapping
    public ResponseData<PageResponseDTO<QuestionResponseDTO>> getQuestions(
            @RequestParam(defaultValue = "",required = false, value = "content") String content,
            @RequestParam(defaultValue = "0", required = false, value = "page-no") int pageNo,
            @RequestParam(defaultValue = "2", required = false, value = "page-size") int pageSize,
            @RequestParam(required = false, value = "categoryIds") List<Long> categoryIds
    ){
        try {
            return new ResponseData<>(questionService.getQuestions(content, categoryIds, pageNo, pageSize)
                    , HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase());
        }catch (Exception e){
            log.error(e.getMessage());
            return new ResponseData<>(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase());
        }
    }

    @GetMapping("/{questionId}")
    public ResponseData<QuestionResponseDTO> getQuestionById(@PathVariable long questionId){
        try {
            return new ResponseData<>(questionService.getQuestionById(questionId), HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase());
        }catch (Exception e){
            log.error(e.getMessage());
            return new ResponseData<>(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase());
        }
    }

    @GetMapping("tests/{testId}")
    public ResponseData<?> getQuestionsInTest(@PathVariable long testId) {
        try {
            return new ResponseData<>(questionService.getQuestionsInTest(testId), HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase());
        } catch (Exception e) {
            return new ResponseData<>(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase());
        }
    }

    @PostMapping
    public ResponseData<Long> addQuestion(
            @RequestPart(value = "dataQuestion") QuestionRequestDTO questionRequestDTO,
            @RequestPart(value = "imageQuestion", required = false) MultipartFile image,
            @RequestPart(value = "imageAnswers", required = false) List<MultipartFile> imageAnswers
    ){
        try {
            return new ResponseData<>(questionService.addQuestion(questionRequestDTO, image, imageAnswers), HttpStatus.CREATED.value(), HttpStatus.CREATED.getReasonPhrase());
        } catch (Exception e){
            e.printStackTrace();
            System.out.println(e.getMessage());
            log.error("Error while add question: {}", e.getMessage());
            return new ResponseData<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
        }
    }

    @PutMapping("/{questionId}")
    public ResponseData<?> updateQuestion(
            @PathVariable Long questionId,
            @RequestPart(value = "dataQuestion") QuestionRequestDTO questionRequestDTO,
            @RequestPart(value = "imageQuestion", required = false) MultipartFile image,
            @RequestPart(value = "imageAnswers", required = false) List<MultipartFile> imageAnswers
    ){
        try {
            questionService.updateQuestion(questionId, questionRequestDTO);
            return new ResponseData<>(HttpStatus.ACCEPTED.value(), HttpStatus.ACCEPTED.getReasonPhrase());
        }
        catch (ResourceNotFoundException e){
            return new ResponseData<>(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            log.error("Error while updating question: {}", e.getMessage());
            return new ResponseData<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
        }
    }

    @PatchMapping("/{questionId}")
    public ResponseData<?> updateCategoriesOfQuestion(@PathVariable Long questionId,
                                                   @RequestBody Map<String, List<Long>> request){
        try {
            questionService.updateCategoriesOfQuestion(questionId, request.get("categoryIds"));
            return new ResponseData<>(HttpStatus.ACCEPTED.value(), HttpStatus.ACCEPTED.getReasonPhrase());
        }
        catch (ResourceNotFoundException e){
            return new ResponseData<>(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
        catch (Exception e){
            return new ResponseData<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
        }
    }

    @DeleteMapping("/{questionId}")
    public ResponseData<?> deleteQuestion(@PathVariable long questionId){
        try {
            questionService.deleteQuestion(questionId);
            return new ResponseData<>(HttpStatus.NO_CONTENT.value(), HttpStatus.NO_CONTENT.getReasonPhrase());
        } catch (Exception e){
            return new ResponseData<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
        }
    }
}