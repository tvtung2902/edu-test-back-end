package com.javaweb.edutest.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface CloudinaryService {
    boolean deleteFile(String file) throws IOException;
    String uploadFile(MultipartFile image) throws IOException;
}
