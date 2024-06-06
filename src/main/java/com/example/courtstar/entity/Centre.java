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

    @Builder.Default
    boolean status = true;
    LocalDate approveDate;

    @OneToMany(mappedBy = "centre")
    Set<Court> courts;
    @OneToMany(mappedBy = "centre")
    Set<Image> images;
    @ManyToOne
    @JoinColumn(name = "manager_id")
    CentreManager manager;
}

