package com.example.vccorp.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.vccorp.dto.request.CategoryRequest;
import com.example.vccorp.dto.response.APIResponse;
import com.example.vccorp.dto.response.CategoryResponse;
import com.example.vccorp.service.CategoryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/categories")
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping("/{id}")
    public ResponseEntity<APIResponse<CategoryResponse>> findById(@PathVariable Long id) {
        CategoryResponse categoryResponse = categoryService.getCategoryById(id);
        APIResponse<CategoryResponse> apiResponse = APIResponse.<CategoryResponse>builder()
                .data(categoryResponse)
                .message("Lấy thông tin category thành công")
                .build();
        return ResponseEntity.ok(apiResponse);
    }
    
    @PostMapping
    public ResponseEntity<APIResponse<CategoryResponse>> createCategory(@RequestBody CategoryRequest categoryRequest) {
        CategoryResponse categoryResponse = categoryService.createCategory(categoryRequest);
        APIResponse<CategoryResponse> apiResponse = APIResponse.<CategoryResponse>builder()
                .data(categoryResponse)
                .message("Tạo category thành công")
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<APIResponse<CategoryResponse>> updateCategory(@PathVariable Long id, @RequestBody CategoryRequest categoryRequest) {
        CategoryResponse categoryResponse = categoryService.updateCategory(id, categoryRequest);
        APIResponse<CategoryResponse> apiResponse = APIResponse.<CategoryResponse>builder()
                .data(categoryResponse)
                .message("Cập nhật category thành công")
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<APIResponse<Void>> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        APIResponse<Void> apiResponse = APIResponse.<Void>builder()
                .message("Xóa category thành công")
                .build();
        return ResponseEntity.ok(apiResponse);
    }
}
