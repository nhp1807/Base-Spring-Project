package com.example.demo.specification;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Predicate;

import com.example.demo.enums.Role;
import com.example.demo.enums.UserStatus;
import com.example.demo.model.User;

public class UserSpecification {
    public static Specification<User> filter(String firstName, String lastName, String email, String phoneNumber, String telegramUsername, String lotusUsername, Role role, UserStatus status, String sortBy, String sortOrder) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (firstName != null) {
                predicates.add(criteriaBuilder.like(root.get("firstName"), "%" + firstName + "%"));
            }
            if (lastName != null) {
                predicates.add(criteriaBuilder.like(root.get("lastName"), "%" + lastName + "%"));
            }
            if (email != null) {
                predicates.add(criteriaBuilder.like(root.get("email"), "%" + email + "%"));
            }
            if (phoneNumber != null) {
                predicates.add(criteriaBuilder.like(root.get("phoneNumber"), "%" + phoneNumber + "%"));
            }
            if (telegramUsername != null) {
                predicates.add(criteriaBuilder.like(root.get("telegramUsername"), "%" + telegramUsername + "%"));
            }
            if (lotusUsername != null) {
                predicates.add(criteriaBuilder.like(root.get("lotusUsername"), "%" + lotusUsername + "%"));
            }
            if (role != null) {
                predicates.add(criteriaBuilder.equal(root.get("role"), role));
            }
            if (status != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), status));
            }

            if (sortBy != null) {
                String order = sortOrder != null ? sortOrder : "asc";
                if (order.equals("asc")) {
                    query.orderBy(criteriaBuilder.asc(root.get(sortBy)));
                } else {
                    query.orderBy(criteriaBuilder.desc(root.get(sortBy)));
                }
            } else {
                query.orderBy(criteriaBuilder.desc(root.get("createdAt")));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
