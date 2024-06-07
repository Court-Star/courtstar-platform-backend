package com.example.courtstar.dto.response;

import com.example.courtstar.entity.Court;
import com.example.courtstar.entity.Image;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class CentreResponse {
    int id;
    int managerId;
    String name;
    String address;
    LocalTime openTime;
    LocalTime closeTime;
    double pricePerHour;
    int slotDuration;
    int numberOfCourt;
    String paymentMethod;
    boolean status;
    LocalDate approveDate;
    Set<Court> courts;
    Set<Image> images;
}