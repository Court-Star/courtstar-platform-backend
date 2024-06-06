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
@RequestMapping("/manager")
public class CentreManagerController {
    @Autowired
    private CentreManagerService centreManagerService;


    @PutMapping("/updateInfo/{id}")
    public ApiResponse<CentreManager> updateManagerCentre(@PathVariable int id ,@RequestBody CentreManagerRequest request) {
        CentreManager centreManager = centreManagerService.updateInformation(id,request);
        return ApiResponse.<CentreManager>builder()
                .data(centreManager)
                .build();
    }
    @PostMapping("/createCentre/{id}")
    public ApiResponse<CentreManager> createCentre(@PathVariable int id , @RequestBody CentreRequest request){
        CentreManager centreManager = centreManagerService.addCentre(id,request);
        return ApiResponse.<CentreManager>builder()
                .data(centreManager)
                .build();
    }
    
}