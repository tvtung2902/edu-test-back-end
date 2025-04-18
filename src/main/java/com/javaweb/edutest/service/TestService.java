package com.javaweb.edutest.service;

import com.javaweb.edutest.dto.request.TestRequestDTO;
import com.javaweb.edutest.dto.response.PageResponseDTO;
import com.javaweb.edutest.dto.response.TestResponseDTO1;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface TestService {
    PageResponseDTO<TestResponseDTO1> getTests(String searchName, Boolean isPublic, int pageNo, int pageSize);
    TestResponseDTO1 getTest(long testId);
    long addTest(TestRequestDTO test, MultipartFile image) throws IOException;
    void updateTest(long testId, TestRequestDTO test, MultipartFile image) throws IOException;
    void deleteTest(long testId) throws IOException;
}
