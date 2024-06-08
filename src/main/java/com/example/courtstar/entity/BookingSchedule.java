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
public class BookingSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    LocalDate date;
    double totalPrice;
    boolean status;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "account_id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    Account account;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "guest_id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    Guest guest;

    @ManyToOne
//    @JsonIgnore
    @JoinColumn(name = "slot_id")
//    @EqualsAndHashCode.Exclude
//    @ToString.Exclude
    Slot slot;

    @ManyToOne
//    @JsonIgnore
    @JoinColumn(name = "court_id")
//    @EqualsAndHashCode.Exclude
//    @ToString.Exclude
    Court court;
}
