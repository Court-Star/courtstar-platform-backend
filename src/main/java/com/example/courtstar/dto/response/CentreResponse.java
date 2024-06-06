package com.example.courtstar.dto.response;

import com.example.courtstar.entity.CentreManager;
import com.example.courtstar.entity.Court;
import jakarta.persistence.OneToMany;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class CentreResponse {
    String name;
    String address;
    LocalDateTime openTime;
    LocalDateTime closeTime;
    int pricePerHour;
    int slotDuration;
    int numberOfCourt;
    String paymentMethod;
    boolean status = true;
    LocalDate approveDate;
    Set<Court> courts;
}