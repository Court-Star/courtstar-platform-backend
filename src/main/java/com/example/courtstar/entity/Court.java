package com.example.courtstar.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Court {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    int courtNo;
    boolean status;
    @ManyToOne
    @JoinColumn(name = "centre_id")
    Centre centre;
}

