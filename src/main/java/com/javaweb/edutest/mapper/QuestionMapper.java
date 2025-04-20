package com.javaweb.edutest.mapper;

import com.javaweb.edutest.dto.request.QuestionRequestDTO;
import com.javaweb.edutest.dto.response.CategoryResponseDTO;
import com.javaweb.edutest.dto.response.QuestionResponseDTO;
import com.javaweb.edutest.model.Category;
import com.javaweb.edutest.model.Question;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface QuestionMapper {
    List<QuestionResponseDTO> toQuestionResponseDTOs(List<Question> questions);
    Question toQuestion(QuestionRequestDTO questionRequestDTO);
    QuestionResponseDTO toQuestionResponseDTO(Question question);
    @Mapping(target = "choices", ignore = true)
    void toQuestion(@MappingTarget Question question, QuestionRequestDTO questionRequestDTO);
    @Mapping(target = "questions", ignore = true)
    CategoryResponseDTO categoryToCategoryResponseDTO(Category category);
}