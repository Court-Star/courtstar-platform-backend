package com.example.courtstar.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Centre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    String name;
    String address;
    LocalDateTime openTime;
    LocalDateTime closeTime;
    int pricePerHour;
    int slotDuration;
    int numberOfCourt;
    String paymentMethod;
    boolean status;
    @Builder.Default
    boolean hoatDong = true;
    LocalDate approveDate;
    @OneToMany
    Set<Court> courts;
    @OneToMany
    Set<Image> images;
}

