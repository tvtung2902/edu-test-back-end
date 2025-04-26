package com.javaweb.edutest.mapper;

import com.javaweb.edutest.dto.request.CategoryRequestDTO;
import com.javaweb.edutest.dto.request.ChoiceRequestDTO;
import com.javaweb.edutest.dto.response.CategoryResponseDTO;
import com.javaweb.edutest.dto.response.ChoiceResponseDTO;
import com.javaweb.edutest.dto.response.GroupResponseDTO;
import com.javaweb.edutest.model.Category;
import com.javaweb.edutest.model.Choice;
import com.javaweb.edutest.model.Group;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ChoiceMapper {
    ChoiceResponseDTO toChoiceResponseDTO(Choice choice);
    List<ChoiceResponseDTO> toChoiceResponseDTOs(List<Choice> choices);
}
