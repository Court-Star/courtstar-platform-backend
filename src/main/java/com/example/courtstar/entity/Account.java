package com.example.courtstar.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Entity
@Table(name = "account")
@FieldDefaults(level = AccessLevel.PRIVATE)
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

    @Column(name = "role_id")
    Integer roleId;

    @Size(max = 30)
    @Column(name = "first_name", length = 30)
    String firstName;

    @Size(max = 30)
    @Column(name = "last_name", length = 30)
    String lastName;

}