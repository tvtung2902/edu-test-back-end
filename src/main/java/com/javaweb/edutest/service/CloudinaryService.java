package com.javaweb.edutest.service;

import com.javaweb.edutest.dto.request.QuestionRequestDTO;
import com.javaweb.edutest.dto.request.TestRequestDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

public interface CloudinaryService {
    String uploadFile(MultipartFile file) throws IOException;
    void uploadFileOfQuestion(QuestionRequestDTO questionRequestDTO,
                              Map<String, MultipartFile> images) throws IOException;
    void uploadFileOfTest(TestRequestDTO testRequestDTO, MultipartFile image) throws IOException;
}
