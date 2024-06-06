package com.example.courtstar.controller;

import com.example.courtstar.dto.request.ApiResponse;
import com.example.courtstar.dto.request.CentreManagerRequest;
import com.example.courtstar.dto.request.CentreRequest;
import com.example.courtstar.dto.response.CentreManagerResponse;
import com.example.courtstar.entity.Centre;
import com.example.courtstar.entity.CentreManager;
import com.example.courtstar.mapper.CentreManagerMapper;
import com.example.courtstar.mapper.CentreMapper;
import com.example.courtstar.services.CentreManagerService;
import com.example.courtstar.services.CentreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/ManagerControler")
public class CentreManagerController {
    @Autowired
    private CentreManagerService centreManagerService;


    @PutMapping("/updateManagerCentre")
    public ApiResponse<CentreManager> updateManagerCentre(@RequestParam String email,@RequestBody CentreManagerRequest request){
        CentreManager centreManager = centreManagerService.updateInformation(email,request);
        return ApiResponse.<CentreManager>builder()
                .data(centreManager)
                .build();
    }
    @PostMapping("/createCentre")
    public ApiResponse<CentreManager> createCentre(@RequestParam String email , @RequestBody CentreRequest request){
        CentreManager centreManager = centreManagerService.addCentre(email,request);
        return ApiResponse.<CentreManager>builder()
                .data(centreManager)
                .build();
    }
    
}