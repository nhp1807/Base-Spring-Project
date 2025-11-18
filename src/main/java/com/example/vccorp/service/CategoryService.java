package com.example.vccorp.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import com.example.vccorp.repository.CategoryRepository;
import com.example.vccorp.enums.ErrorCode;
import com.example.vccorp.exception.AppException;
import com.example.vccorp.mapper.CategoryMapper;
import com.example.vccorp.model.Category;
import com.example.vccorp.dto.response.CategoryResponse;
import com.example.vccorp.dto.request.CategoryRequest;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryResponse getCategoryById(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));
        return CategoryMapper.toResponse(category);
    }

    public CategoryResponse createCategory(CategoryRequest request) {
        Category category = CategoryMapper.toEntity(request);
        if (categoryRepository.findByName(request.getName()).isPresent()) {
            throw new AppException(ErrorCode.CATEGORY_ALREADY_EXISTS);
        }
        Category savedCategory = categoryRepository.save(category);
        return CategoryMapper.toResponse(savedCategory);
    }

    public CategoryResponse updateCategory(Long id, CategoryRequest request) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));
        if (request.getName() != null) {
            category.setName(request.getName());
        }
        Category updatedCategory = categoryRepository.save(category);
        return CategoryMapper.toResponse(updatedCategory);
    }
    
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));
        categoryRepository.delete(category);
    }
}
