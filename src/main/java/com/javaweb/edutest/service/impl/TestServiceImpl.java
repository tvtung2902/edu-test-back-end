package com.javaweb.edutest.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.javaweb.edutest.dto.request.TestRequestDTO;
import com.javaweb.edutest.dto.response.CategoryWithQuestionCountDTO;
import com.javaweb.edutest.dto.response.PageResponseDTO;
import com.javaweb.edutest.dto.response.TestResponseDTO;
import com.javaweb.edutest.dto.response.TestResponseDTO1;
import com.javaweb.edutest.exception.ResourceNotFoundException;
import com.javaweb.edutest.mapper.TestMapper;
import com.javaweb.edutest.model.Test;
import com.javaweb.edutest.repository.SearchTestsRepository;
import com.javaweb.edutest.repository.TestRepository;
import com.javaweb.edutest.service.CloudinaryService;
import com.javaweb.edutest.service.TestService;
import com.javaweb.edutest.util.PaginationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {
    private final TestMapper testMapper;
    private final TestRepository testRepository;
    private final CloudinaryService cloudinaryService;
    private final SearchTestsRepository searchTestsRepository;

    @Override
    public PageResponseDTO<TestResponseDTO1> getTests(String searchName, Boolean isPublic, int pageNo, int pageSize) {
        int totalRecords = isPublic == null ? testRepository.countByNameContainingIgnoreCase(searchName) :
                testRepository.countTests(searchName, isPublic) ;
        Pageable pageable = PaginationUtil.createPageable(pageNo, pageSize, totalRecords);
//        Page<TestResponseDTO> testResponseDTOs = testRepository.findTests(searchName, pageable);
        Page<TestResponseDTO1> testResponseDTO1s = searchTestsRepository.findTests(searchName, isPublic, pageable, totalRecords);
        return PaginationUtil.toPageResponse(testResponseDTO1s);
    }

    @Override
    public TestResponseDTO1 getTest(long testId) {
        return testMapper.toTestResponseDTO(findTestById(testId));
    }


    @Override
    public long addTest(TestRequestDTO testRequestDTO, MultipartFile image) throws IOException {
        Test newTest = testMapper.toTest(testRequestDTO);
        try{
            if (image != null){
                String imageUrl = cloudinaryService.uploadFileOfTest(image);
                newTest.setImage(imageUrl);
            }
        } finally {
            testRepository.save(newTest);
        }
        return newTest.getId();
    }

    @Override
    public void updateTest(long testId, TestRequestDTO test, MultipartFile image) throws IOException {
        var currentTest = findTestById(testId);
        try {
            if (image != null){
                String imageUrl = cloudinaryService.uploadFileOfTest(image);
                currentTest.setImage(imageUrl);
            }
            else {
                currentTest.setImage(null);
            }
        } finally {
            testMapper.updateTest(currentTest, test);
            testRepository.save(currentTest);
        }
    }

    @Override
    public void deleteTest(long testId) throws IOException {
        String image = findTestById(testId).getImage();
        try {
            cloudinaryService.deleteFile(image);
        } finally {
            testRepository.deleteById(testId);
        }
    }

    private Test findTestById(long testId){
        return testRepository.findById(testId).orElseThrow(
                () -> new ResourceNotFoundException("test not found with testId " +testId)
        );
    }
}