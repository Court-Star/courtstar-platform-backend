package com.example.courtstar.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalTime;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Slot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    int slotNo;
    LocalTime startTime;
    LocalTime endTime;

    @ManyToOne
    @JoinColumn(name = "centre_id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonIgnore
    Centre centre;

    @OneToMany(mappedBy = "slot", cascade = CascadeType.ALL, orphanRemoval = true)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonIgnore
    List<BookingSchedule> bookingSchedules;

    @OneToMany(mappedBy = "slot", cascade = CascadeType.ALL, orphanRemoval = true)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonIgnore
    List<SlotUnavailable> slotUnavailables;
}
