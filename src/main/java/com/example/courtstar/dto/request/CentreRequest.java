package com.example.courtstar.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class CentreRequest {
    String name;
    String address;
    LocalTime openTime;
    LocalTime closeTime;
    double pricePerHour;
    int numberOfCourt;
    String paymentMethod;
    LocalDate approveDate;
    List<String> images;
}