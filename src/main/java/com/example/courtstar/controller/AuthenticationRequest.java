package com.example.courtstar.controller;

import com.example.courtstar.dto.request.ApiResponse;
import com.example.courtstar.dto.request.AuthenticationRequuest;
import com.example.courtstar.dto.response.AuthenticationResponse;
import com.example.courtstar.service.AccountAuthentication;
import com.nimbusds.jose.JOSEException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationRequest {
    @Autowired
    private AccountAuthentication authentication;

    @PostMapping("/token")
    public ApiResponse<AuthenticationResponse> authenticate(@RequestBody @Valid AuthenticationRequuest requuest) throws JOSEException {
        AuthenticationResponse authenticationResponse =authentication.Authenticate(requuest);
        return ApiResponse.<AuthenticationResponse>builder()
                .data(authenticationResponse)
                .code(1000)
                .message("Login Success")
                .build();
    }
}
