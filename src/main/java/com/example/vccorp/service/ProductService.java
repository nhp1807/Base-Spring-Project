package com.example.vccorp.service;

import java.util.ArrayList;
import java.util.List;

import com.example.vccorp.dto.response.CategoryResponse;
import com.example.vccorp.mapper.CategoryMapper;
import org.springframework.stereotype.Service;

import com.example.vccorp.enums.ErrorCode;
import com.example.vccorp.exception.AppException;
import com.example.vccorp.mapper.ProductMapper;
import com.example.vccorp.model.Category;
import com.example.vccorp.model.Product;
import com.example.vccorp.dto.response.ProductCategoryResponse;
import com.example.vccorp.dto.response.ProductResponse;
import com.example.vccorp.dto.request.ProductRequest;
import com.example.vccorp.repository.ProductRepository;
import com.example.vccorp.repository.CategoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductResponse getProductById(Long id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));
        ProductResponse response = ProductMapper.toResponse(product);
        response.setCategory(CategoryMapper.toResponse(product.getCategory()));
        return response;
    }

    public List<ProductCategoryResponse> getAllProductCategory() {
        return productRepository.findAllProductCategory();
    }

    public List<ProductResponse> getProductsByCategoryId(Long categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));
        CategoryResponse categoryResponse = CategoryMapper.toResponse(category);
        List<Product> products = productRepository.findByCategory_Id(categoryId);
        List<ProductResponse> responses = new ArrayList<>();
        for (Product product : products) {
            ProductResponse response = ProductMapper.toResponse(product);
            response.setCategory(categoryResponse);
            responses.add(response);
        }
        return responses;
    }
    
    public ProductResponse createProduct(ProductRequest request) {
        if (productRepository.findByName(request.getName()).isPresent()) {
            throw new AppException(ErrorCode.PRODUCT_ALREADY_EXISTS);
        }

        Category category = categoryRepository.findById(request.getCategoryId()).orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));
        
        Product product = ProductMapper.toEntity(request);
        Product savedProduct = productRepository.save(product);
        savedProduct.setCategory(category);
        return ProductMapper.toResponse(savedProduct);
    }

    public ProductResponse updateProduct(Long id, ProductRequest request) {
        Product product = productRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));
        if (request.getName() != null) {
            product.setName(request.getName());
        }
        if (request.getCategoryId() != null) {
            Category category = categoryRepository.findById(request.getCategoryId()).orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));
            product.setCategory(category);
        }

        Product updatedProduct = productRepository.save(product);
        return ProductMapper.toResponse(updatedProduct);
    }

    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));
        productRepository.delete(product);
    }
}
