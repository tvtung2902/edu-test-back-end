package com.javaweb.edutest.service;

import com.javaweb.edutest.dto.request.CategoryRequestDTO;
import com.javaweb.edutest.dto.response.CategoryResponseDTO;
import com.javaweb.edutest.dto.response.PageResponseDTO;

import java.util.List;

public interface CategoryService {
    List<CategoryResponseDTO> getCategories();
    PageResponseDTO<?> getCategories(int pageNo, int pageSize, String searchName, boolean showAll);
    CategoryResponseDTO getCategory(long categoryId);
    long addCategory(CategoryRequestDTO user);
    void updateCategory(long categoryId, CategoryRequestDTO user);
    void deleteCategory(long categoryId);
}
