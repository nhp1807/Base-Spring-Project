package com.example.vccorp.mapper;

import com.example.vccorp.dto.response.CategoryResponse;
import com.example.vccorp.dto.request.CategoryRequest;
import com.example.vccorp.model.Category;

public class CategoryMapper {
    public static CategoryResponse toResponse(Category category) {
        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName() != null ? category.getName().trim() : null)
                .build();
    }

    public static Category toEntity(CategoryRequest request) {
        return Category.builder()
                .name(request.getName() != null ? request.getName().trim() : null)
                .build();
    }
}
