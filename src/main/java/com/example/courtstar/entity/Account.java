package com.example.courtstar.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "account")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id", nullable = false)
    Integer id;

    @Size(max = 80)
    @Column(name = "email", length = 80)
    String email;

    @Size(max = 255)
    @Column(name = "password", length = 255)
    String password;

    @Size(max = 10)
    @Column(name = "phone", length = 10)
    String phone;

    @Column(name = "role")
    Set<String> role;


    @Size(max = 30)
    @Column(name = "first_name", length = 30)
    String firstName;

    @Size(max = 30)
    @Column(name = "last_name", length = 30)
    String lastName;

}