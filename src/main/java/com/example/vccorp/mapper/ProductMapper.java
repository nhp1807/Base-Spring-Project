package com.example.vccorp.mapper;

import com.example.vccorp.dto.request.ProductRequest;
import com.example.vccorp.dto.response.ProductResponse;
import com.example.vccorp.model.Category;
import com.example.vccorp.model.Product;

public class ProductMapper {

    public static Product toEntity(ProductRequest request) {
        return Product.builder()
                .name(request.getName() != null ? request.getName().trim() : null)
                .category(Category.builder()
                        .id(request.getCategoryId())
                        .build())
                .build();
    }

    public static ProductResponse toResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName() != null ? product.getName().trim() : null)
                .build();
    }
}
