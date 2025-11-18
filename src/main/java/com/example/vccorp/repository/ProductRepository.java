package com.example.vccorp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.vccorp.model.Product;
import com.example.vccorp.dto.response.ProductCategoryResponse;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findByName(String name);

    // Lấy danh sách product và category theo cấu trúc ProductCategoryResponse
    @Query("SELECT p.id as id, p.name as productName, c.name as categoryName FROM Product p JOIN p.category c")
    List<ProductCategoryResponse> findAllProductCategory();

    @Query("SELECT p FROM Product p JOIN FETCH p.category")
    List<Product> findAllWithCategory();

    List<Product> findByCategory_Id(Long categoryId);
}
