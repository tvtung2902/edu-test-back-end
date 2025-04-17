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
    @Override
    public String uploadFileToCloudinary(MultipartFile file) throws IOException {
        Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
        return uploadResult.get("secure_url").toString();
    }

//    @Override
//    public void uploadFileOfQuestion(QuestionRequestDTO questionRequestDTO, Map<String, MultipartFile> images) throws IOException {
//        if (images.get("imageQuestion") != null){
//            String imageQuestionUrl = uploadFileToCloudinary(images.get("imageQuestion"));
//            questionRequestDTO.setImage(imageQuestionUrl);
//        }
//        List<ChoiceRequestDTO> choices = questionRequestDTO.getChoices();
//        if(choices != null && !choices.isEmpty()) {
//            for (int i = 0; i < choices.size(); i++){
//                String key = "imageChoices" + i;
//                if(images.containsKey(key)){
//                    MultipartFile image = images.get(key);
//                    if (image != null && !image.isEmpty()){
//                        String imageChoiceUrl = uploadFileToCloudinary(image);
//                        choices.get(i).setImage(imageChoiceUrl);
//                    }
//                }
//            }
//        }
//    }

    @Override
    public String uploadFile(MultipartFile image) throws IOException {
        if (image != null){
            return uploadFileToCloudinary(image);
        }
        return null;
    }

    @Override
    public boolean deleteFile(String publicId) throws IOException {
        if (publicId == null || publicId.trim().isEmpty()) {
            return false;
        }
        Map result = cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
        return "ok".equals(result.get("result"));
    }
}
