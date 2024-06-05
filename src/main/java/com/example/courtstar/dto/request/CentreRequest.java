package com.example.courtstar.dto.request;

import com.example.courtstar.entity.Image;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class CentreRequest {
    String name;
    String address;
    LocalDateTime openTime;
    LocalDateTime closeTime;
    int pricePerHour;
    int slotDuration;
    int numberOfCourt;
    String paymentMethod;
    boolean status;
    LocalDate approveDate;
    List<Image> images;
}