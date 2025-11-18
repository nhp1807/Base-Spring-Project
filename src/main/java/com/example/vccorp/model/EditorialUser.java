package com.example.vccorp.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@DiscriminatorValue("2")
public class EditorialUser extends User {
    private String department;
}
