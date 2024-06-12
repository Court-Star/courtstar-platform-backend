package com.example.courtstar.controller;

import com.example.courtstar.dto.request.ApiResponse;
import com.example.courtstar.dto.request.CheckInRequest;
import com.example.courtstar.services.CheckInService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/account")
public class CheckInController {
    @Autowired
    private CheckInService checkInService;

    @PostMapping("/checkin")
    public ApiResponse<Boolean> CheckIn(@RequestBody CheckInRequest checkInRequest){
        return ApiResponse.<Boolean>builder()
                .data(checkInService.checkInBooking(checkInRequest.getEmail()
                        ,checkInRequest.getCourt(),checkInRequest.getSlot()))
                .build();
    }

}
