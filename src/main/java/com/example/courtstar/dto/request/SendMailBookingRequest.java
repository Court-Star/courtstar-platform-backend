package com.example.courtstar.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SendMailBookingRequest {
    String email;
    String firstName;
    String lastName;
    String phone;
    int number_Court;
    double price;
    int booking_id;
}
