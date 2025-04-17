package com.javaweb.edutest.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface CloudinaryService {
    String uploadFileToCloudinary(MultipartFile file) throws IOException;
    boolean deleteFile(String publicId) throws IOException;
    String uploadFile(MultipartFile image) throws IOException;
}
