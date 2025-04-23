package com.javaweb.edutest.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.javaweb.edutest.service.CloudinaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class CloudinaryServiceImpl implements CloudinaryService {
    private final Cloudinary cloudinary;
    private String uploadFileToCloudinary(MultipartFile file) throws IOException {
        Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
        return uploadResult.get("secure_url").toString();
    }

    @Override
    public String uploadFile(MultipartFile image) throws IOException {
        if (image != null){
            return uploadFileToCloudinary(image);
        }
        return null;
    }

    @Override
    public boolean deleteFile(String file) throws IOException {
        if (file == null || file.trim().isEmpty()) {
            return false;
        }
        Map result = cloudinary.uploader().destroy(file, ObjectUtils.emptyMap());
        return "ok".equals(result.get("result"));
    }
}
