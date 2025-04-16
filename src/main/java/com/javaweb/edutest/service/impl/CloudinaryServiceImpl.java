package com.javaweb.edutest.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.javaweb.edutest.dto.request.ChoiceRequestDTO;
import com.javaweb.edutest.dto.request.QuestionRequestDTO;
import com.javaweb.edutest.dto.request.TestRequestDTO;
import com.javaweb.edutest.service.CloudinaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.List;


@Service
@RequiredArgsConstructor
public class CloudinaryServiceImpl implements CloudinaryService {
    private final Cloudinary cloudinary;
    @Override
    public String uploadFile(MultipartFile file) throws IOException {
        Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
        return uploadResult.get("secure_url").toString();
    }

    @Override
    public void uploadFileOfQuestion(QuestionRequestDTO questionRequestDTO, Map<String, MultipartFile> images) throws IOException {
        if (images.get("imageQuestion") != null){
            String imageQuestionUrl = uploadFile(images.get("imageQuestion"));
            questionRequestDTO.setImage(imageQuestionUrl);
        }
        List<ChoiceRequestDTO> choices = questionRequestDTO.getChoices();
        if(choices != null && !choices.isEmpty()) {
            for (int i = 0; i < choices.size(); i++){
                String key = "imageChoices" + i;
                if(images.containsKey(key)){
                    MultipartFile image = images.get(key);
                    if (image != null && !image.isEmpty()){
                        String imageChoiceUrl = uploadFile(image);
                        choices.get(i).setImage(imageChoiceUrl);
                    }
                }
            }
        }
    }

    @Override
    public void uploadFileOfTest(TestRequestDTO testRequestDTO, MultipartFile image) throws IOException {
//        if (image != null){
//            String imageQuestionUrl = uploadFile(image);
//            testRequestDTO.setImage(imageQuestionUrl);
//        }
    }
}
