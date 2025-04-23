package com.javaweb.edutest.controller;

import com.javaweb.edutest.dto.request.QuestionRequestDTO;
import com.javaweb.edutest.dto.request.QuestionTestRequestDTO;
import com.javaweb.edutest.dto.request.TestRequestDTO;
import com.javaweb.edutest.dto.response.ResponseData;
import com.javaweb.edutest.dto.response.TestResponseDTO1;
import com.javaweb.edutest.service.QuestionService;
import com.javaweb.edutest.service.TestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/api/tests")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class TestController {
    private final TestService testService;
    private final QuestionService questionService;

    @GetMapping
    public ResponseData<?> getTests(
            @RequestParam(defaultValue = "", required = false, value = "name") String searchName,
            @RequestParam(required = false, value = "public") Boolean isPublic,
            @RequestParam(defaultValue = "0", required = false, value = "page-no") int pageNo,
            @RequestParam(defaultValue = "2", required = false, value = "page-size") int pageSize
    ) {
        try {
            return new ResponseData<>(testService.getTests(searchName, isPublic, pageNo, pageSize), HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase());
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseData<>(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase());
        }
    }

    @GetMapping("/{testId}")
    public ResponseData<TestResponseDTO1> getTest(@PathVariable long testId) {
        try {
            return new ResponseData<>(testService.getTest(testId), HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase());
        } catch (Exception e) {
            return new ResponseData<>(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase());
        }
    }

    @PostMapping
    public ResponseData<?> addTest(@RequestPart("data") @Valid TestRequestDTO testRequestDTO,
                                   @RequestPart(value = "imageUrl", required = false) MultipartFile image) {
        try {
            return new ResponseData<>(testService.addTest(testRequestDTO, image), HttpStatus.CREATED.value(), HttpStatus.CREATED.getReasonPhrase());
        }
        catch (IOException e){
            return new ResponseData<>(HttpStatus.OK.value(), "Test created, but image upload failed");
        }
        catch (Exception e) {
            return new ResponseData<>(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase());
        }
    }

    @PostMapping("/{testId}/question")
    public ResponseData<?> addQuestionsToTest(@PathVariable long testId,
                                              @RequestPart(value = "dataQuestion") QuestionRequestDTO questionRequestDTO,
                                              @RequestPart(value = "imageQuestion", required = false) MultipartFile image,
                                              @RequestPart(value = "imageAnswers", required = false) List<MultipartFile> imageAnswers
                                             ){
        try {
            return new ResponseData<>(questionService.addQuestionToTest(testId, questionRequestDTO, image, imageAnswers), HttpStatus.CREATED.value(), HttpStatus.CREATED.getReasonPhrase());
        }
        catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return new ResponseData<>(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase());
        }
    }

    @PostMapping("/{testId}/questions/from-library")
    public ResponseData<?> addQuestionsFromLibraryToTest(@PathVariable long testId, @RequestBody QuestionTestRequestDTO request) {
        try {
            questionService.addQuestionFromLibraryToTest(testId, request);
            return new ResponseData<>(HttpStatus.CREATED.value(), HttpStatus.CREATED.getReasonPhrase());
        } catch (Exception e) {
            return new ResponseData<>(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase());
        }
    }

    @PutMapping("/{testId}")
    public ResponseData<?> updateTest(@PathVariable long testId,
                                      @RequestPart("data") @Valid TestRequestDTO testRequestDTO,
                                      @RequestPart(value = "imageUrl", required = false) MultipartFile image
    ) {
        try {
            testService.updateTest(testId, testRequestDTO, image);
            return new ResponseData<>(HttpStatus.ACCEPTED.value(), HttpStatus.ACCEPTED.getReasonPhrase());
        }
        catch (IOException e){
            return new ResponseData<>(HttpStatus.OK.value(), "Test updated, but has issue with image");
        }
        catch (Exception e) {
            return new ResponseData<>(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase());
        }
    }

    @DeleteMapping("/{testId}")
    public ResponseData<?> deleteTest(@PathVariable long testId) {
        try {
            testService.deleteTest(testId);
            return new ResponseData<>(HttpStatus.NO_CONTENT.value(), HttpStatus.NO_CONTENT.getReasonPhrase());
        }
        catch (IOException e){
            return new ResponseData<>(HttpStatus.NO_CONTENT.value(), HttpStatus.NO_CONTENT.getReasonPhrase());
        }
        catch (Exception e) {
            return new ResponseData<>(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase());
        }
    }

    @DeleteMapping("/{testId}/question/{questionId}")
    public ResponseData<?> updateQuestionsInTest(@PathVariable long testId, @PathVariable long questionId) {
        try {
            return null;
        } catch (Exception e) {
            return new ResponseData<>(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase());
        }
    }
}
