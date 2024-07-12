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
public class BookingDetailResponse {
    LocalDate date;
    Court court;
    Slot slot;
    boolean status = false;
}
