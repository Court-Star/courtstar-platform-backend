package com.example.courtstar.dto.response;

import com.example.courtstar.entity.Account;
import com.example.courtstar.entity.Court;
import com.example.courtstar.entity.Guest;
import com.example.courtstar.entity.Slot;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;

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
    List<Slot> slots;
    Court court;
    Account account;
    Guest guest;
    String centreName;
    String centreAddress;
    String centreImg;
    int rate;
    boolean status;
    boolean success;
}
