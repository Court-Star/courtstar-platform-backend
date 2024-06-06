package com.example.courtstar.controller;

import com.example.courtstar.dto.request.ApiResponse;
import com.example.courtstar.dto.request.CentreRequest;
import com.example.courtstar.dto.response.CentreResponse;
import com.example.courtstar.dto.response.CourtResponse;
import com.example.courtstar.mapper.CentreMapper;
import com.example.courtstar.services.CentreService;
import com.example.courtstar.services.CourtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/Court")
public class CourtController {
    @Autowired
    private CourtService courtService;

    @GetMapping("/GetAllCourt")
    public ApiResponse<List<CourtResponse>> GetAllCourt() {
        return ApiResponse.<List<CourtResponse>>builder()
                .data(courtService.getAllCourts())
                .build();
    }

}