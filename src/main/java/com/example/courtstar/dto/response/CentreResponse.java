package com.example.courtstar.dto.response;

import com.example.courtstar.entity.CentreStaff;
import com.example.courtstar.entity.Court;
import com.example.courtstar.entity.Image;
import com.example.courtstar.entity.Slot;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

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
    boolean isDelete;
    LocalDate approveDate;
    List<Court> courts;
    List<Image> images;
    List<Slot> slots;
//    List<CentreStaff> centreStaffs;

    @Builder.Default
    int rating = ThreadLocalRandom.current().nextInt(1, 6);
}