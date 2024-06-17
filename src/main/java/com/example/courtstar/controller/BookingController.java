package com.example.courtstar.controller;

import com.example.courtstar.dto.request.ApiResponse;
import com.example.courtstar.dto.request.BookingRequest;
import com.example.courtstar.dto.response.BookingScheduleResponse;
import com.example.courtstar.entity.BookingSchedule;
import com.example.courtstar.services.BookingService;
import com.google.zxing.WriterException;
import jakarta.mail.MessagingException;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/booking")
public class BookingController {

    @Autowired
    BookingService bookingService;

    @PostMapping
    public ApiResponse<BookingSchedule> booking(@RequestBody BookingRequest request) throws IOException, JSONException {
        ApiResponse apiResponse = ApiResponse.builder()
                .data(bookingService.booking(request))
                .build();
        return apiResponse;
    }

    @GetMapping("/{centreId}")
    public ApiResponse<List<BookingSchedule>> getBookingSchedules(@PathVariable int centreId){
        ApiResponse apiResponse = ApiResponse.builder()
                .data(bookingService.getBookingSchedules(centreId))
                .build();
        return apiResponse;
    }

    @GetMapping()
    public ApiResponse<List<BookingScheduleResponse>> getBookingSchedulesOfAccount(){
        ApiResponse apiResponse = ApiResponse.builder()
                .data(bookingService.getBookingSchedulesOfAccount())
                .build();
        return apiResponse;
    }
}
