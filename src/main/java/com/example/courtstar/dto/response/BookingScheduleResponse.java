package com.example.courtstar.dto.response;

import com.example.courtstar.entity.Court;
import com.example.courtstar.entity.Slot;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class BookingScheduleResponse {

    Integer id;
    Integer centreId;
    LocalDate date;
    double totalPrice;
    Slot slot;
    Court court;
    String centreName;
    String centreAddress;
    int rate;
}
