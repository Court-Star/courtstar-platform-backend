package com.example.courtstar.controller;

import com.example.courtstar.dto.request.ApiResponse;
import com.example.courtstar.dto.request.CentreRequest;
import com.example.courtstar.dto.response.CentreResponse;
import com.example.courtstar.entity.Centre;
import com.example.courtstar.mapper.AccountMapper;
import com.example.courtstar.mapper.CentreMapper;
import com.example.courtstar.services.AccountService;
import com.example.courtstar.services.CentreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/centre")
public class CentreController {
    @Autowired
    private CentreService centreService;
    @Autowired
    private CentreMapper centreMapper;

    @GetMapping("/allCentre")
    public ApiResponse<List<CentreResponse>> GetAllCentre(){
        return ApiResponse.<List<CentreResponse>>builder()
                .data(centreService.getAllCentres())
                .build();
    }


    @PutMapping("/updateCentre/{id}")
    public ApiResponse<CentreResponse> updateCentre(@PathVariable int id, @RequestBody CentreRequest request){
        CentreResponse centreResponse =centreService.UpdateCentre(id, request);
        return ApiResponse.<CentreResponse>builder()
                .data(centreResponse)
                .build();
    }

    @GetMapping("/getAllCentresOfManager")
    public ApiResponse<Set<CentreResponse>> GetAllCentresOfManager(){
       var context = SecurityContextHolder.getContext();
       String email = context.getAuthentication().getName();
        return ApiResponse.<Set<CentreResponse>>builder()
                .data(centreService.getAllCentresOfManager(email)).build();
    }

    @GetMapping("/getCentre/{id}")
    public ApiResponse<CentreResponse> GetCentre(@PathVariable int id){
        return ApiResponse
                .<CentreResponse>builder().data(centreService.getCentre(id)).build();
    }


    @GetMapping("/getAllCentreActive")
    public ApiResponse<List<CentreResponse>> GetAllCentreActive(){
        var context = SecurityContextHolder.getContext();
        String email = context.getAuthentication().getName();
        return ApiResponse.<List<CentreResponse>>builder()
                .data(centreService.getAllCentresIsActive(email,true))
                .build();
    }

    @GetMapping("/getAllCentreDisable")
    public ApiResponse<List<CentreResponse>> GetAllCentreDisable(){
        var context = SecurityContextHolder.getContext();
        String email = context.getAuthentication().getName();
        return ApiResponse.<List<CentreResponse>>builder()
                .data(centreService.getAllCentresIsActive(email,false))
                .build();
    }

    @PostMapping("/disableCentre/{id}")
    public ApiResponse<CentreResponse> disableCentre(@PathVariable int id){
        return ApiResponse.<CentreResponse>builder()
                .data(centreService.isActive(id,false))
                .build();
    }

    @PostMapping("/activeCentre/{id}")
    public ApiResponse<CentreResponse> ActiveCentre(@PathVariable int id){
        return ApiResponse.<CentreResponse>builder()
                .data(centreService.isActive(id,true))
                .build();
    }
}