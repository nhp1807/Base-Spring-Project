package com.example.vccorp.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.vccorp.dto.request.ProductRequest;
import com.example.vccorp.dto.response.APIResponse;
import com.example.vccorp.dto.response.ProductCategoryResponse;
import com.example.vccorp.dto.response.ProductResponse;
import com.example.vccorp.service.ProductService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
public class ProductController {
    private final ProductService productService;


    @GetMapping("/{id}")
    public ResponseEntity<APIResponse<ProductResponse>> findById(@PathVariable Long id) {
        ProductResponse productResponse = productService.getProductById(id);
        APIResponse<ProductResponse> apiResponse = APIResponse.<ProductResponse>builder()
                .data(productResponse)
                .message("Lấy thông tin product thành công")
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/all")
    public ResponseEntity<APIResponse<List<ProductCategoryResponse>>> getAllProductCategory() {
        List<ProductCategoryResponse> productCategoryResponses = productService.getAllProductCategory();
        APIResponse<List<ProductCategoryResponse>> apiResponse = APIResponse.<List<ProductCategoryResponse>>builder()
                .data(productCategoryResponses)
                .message("Lấy tất cả product category thành công")
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<APIResponse<List<ProductResponse>>> getProductsByCategoryId(@PathVariable Long categoryId) {
        List<ProductResponse> productResponses = productService.getProductsByCategoryId(categoryId);
        APIResponse<List<ProductResponse>> apiResponse = APIResponse.<List<ProductResponse>>builder()
                .data(productResponses)
                .message("Lấy products theo categoryId thành công")
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping
    public ResponseEntity<APIResponse<ProductResponse>> createProduct(@RequestBody ProductRequest productRequest) {
        ProductResponse productResponse = productService.createProduct(productRequest);
        APIResponse<ProductResponse> apiResponse = APIResponse.<ProductResponse>builder()
                .data(productResponse)
                .message("Tạo product thành công")
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<APIResponse<ProductResponse>> updateProduct(@PathVariable Long id, @RequestBody ProductRequest productRequest) {
        ProductResponse productResponse = productService.updateProduct(id, productRequest);
        APIResponse<ProductResponse> apiResponse = APIResponse.<ProductResponse>builder()
                .data(productResponse)
                .message("Cập nhật product thành công")
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<APIResponse<Void>> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        APIResponse<Void> apiResponse = APIResponse.<Void>builder()
                .message("Xóa product thành công")
                .build();
        return ResponseEntity.ok(apiResponse);
    }
}
