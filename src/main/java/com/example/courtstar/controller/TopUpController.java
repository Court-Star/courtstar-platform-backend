package com.example.courtstar.controller;

import com.example.courtstar.dto.request.ApiResponse;
import com.example.courtstar.entity.TopUp;
import com.example.courtstar.entity.TransferMoney;
import com.example.courtstar.services.TopUpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = {"http://localhost:3000", "https://courtstar-platform-frontend.vercel.app/"})
@RestController
@RequestMapping("/top-up")
public class TopUpController {

    @Autowired
    private TopUpService topUpService;

    @GetMapping("/all")
    public ApiResponse<List<TopUp>> getAllTopUpOfManager(){
        return ApiResponse.<List<TopUp>>builder()
                .data(topUpService.getAllTopUpOfManager())
                .build();
    }
}
