package com.example.vccorp.repository;

import com.example.vccorp.model.BrandUser;
import com.example.vccorp.model.EditorialUser;
import com.example.vccorp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;



import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    List<User> findByRole(Integer role);

    // Query theo loại user
    @Query("SELECT u FROM User u WHERE TYPE(u) = EditorialUser")
    List<EditorialUser> findAllEditorialUsers();

    @Query("SELECT u FROM User u WHERE TYPE(u) = EditorialUser AND u.id = :id")
    Optional<EditorialUser> findEditorialUserById(Long id);
    
    @Query("SELECT u FROM User u WHERE TYPE(u) = BrandUser") 
    List<BrandUser> findAllBrandUsers();

    @Query("SELECT u FROM User u WHERE TYPE(u) = BrandUser AND u.id = :id")
    Optional<BrandUser> findBrandUserById(Long id);
}
