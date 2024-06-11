package com.example.courtstar.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SlotUnavailable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    LocalDate date;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "court_id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    Court court;

    @ManyToOne
    @JoinColumn(name = "slot_id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    Slot slot;
}
