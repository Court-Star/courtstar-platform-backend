package com.example.courtstar.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @Builder.Default
    boolean status = true;

    @ManyToOne
    @JoinColumn(name = "centre_id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonIgnore
    Centre centre;
}

