package com.example.courtstar.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Entity
@Data
@Table(name = "centre")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Centre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    Integer id;

    @Size(max = 50)
    @Column(name = "name", length = 50)
    String name;

    @Size(max = 100)
    @Column(name = "address", length = 100)
    String address;

    @Size(max = 30)
    @Column(name = "district", length = 30)
    String district;

    @Column(name = "open_time")
    LocalTime openTime;

    @Column(name = "close_time")
    LocalTime closeTime;

    @Column(name = "price_per_hour")
    double pricePerHour;

    @Builder.Default
    @Column(name = "slot_duration")
    int slotDuration = 1;

    @Column(name = "number_of_courts")
    int numberOfCourts;

    @Column(name = "description")
    String description;

    @Column(name = "status")
    @Builder.Default
    boolean status = true;

    @Column(name = "approve_date")
    LocalDate approveDate;

    @Column(name = "revenue")
    @Builder.Default
    double revenue = 0;

    @OneToMany(mappedBy = "centre", cascade = CascadeType.ALL, orphanRemoval = true)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    List<Court> courts;

    @OneToMany(mappedBy = "centre", cascade = CascadeType.ALL, orphanRemoval = true)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    List<Slot> slots;

    @OneToMany(mappedBy = "centre", cascade = CascadeType.ALL, orphanRemoval = true)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    List<Image> images;

    @OneToMany(mappedBy = "centre", cascade = CascadeType.ALL, orphanRemoval = true)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    List<CentreStaff> centreStaffs;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "manager_id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    CentreManager manager;

    @Builder.Default
    @Column(name = "is_delete")
    boolean isDelete = false;

    @OneToOne
    PaymentMethod paymentMethod;

}

