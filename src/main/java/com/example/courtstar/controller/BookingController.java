package com.example.courtstar.controller;

import com.example.courtstar.dto.request.ApiResponse;
import com.example.courtstar.dto.request.BookingRequest;
import com.example.courtstar.entity.BookingSchedule;
import com.example.courtstar.services.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/booking")
public class BookingController {

    @Autowired
    BookingService bookingService;

    @PostMapping
    public ApiResponse<BookingSchedule> booking(@RequestBody BookingRequest request){
        ApiResponse apiResponse = ApiResponse.builder()
                .data(bookingService.booking(request))
                .build();
        return apiResponse;
    }
}
