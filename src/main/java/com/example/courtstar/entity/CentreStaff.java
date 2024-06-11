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
public class CentreStaff {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @OneToOne
    Account account;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "centre_id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    Centre centre;
}
