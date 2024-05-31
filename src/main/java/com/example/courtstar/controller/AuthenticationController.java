package com.example.courtstar.controller;

import com.example.courtstar.dto.request.ApiResponse;
import com.example.courtstar.dto.request.AuthenticationRequest;
import com.example.courtstar.dto.request.IntrospectRequest;
import com.example.courtstar.dto.request.LogoutRequest;
import com.example.courtstar.dto.response.AuthenticationResponse;
import com.example.courtstar.dto.response.IntrospectResponse;
import com.example.courtstar.services.AccountAuthentication;
import com.nimbusds.jose.JOSEException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    @Autowired
    private AccountAuthentication authentication;

    @PostMapping("/token")
    public ApiResponse<AuthenticationResponse> authenticate(@RequestBody @Valid AuthenticationRequest request) throws JOSEException {
        AuthenticationResponse authenticationResponse =authentication.Authenticate(request);
        return ApiResponse.<AuthenticationResponse>builder()
                .data(authenticationResponse)
                .code(1000)
                .message("Login Success")
                .build();
    }
    @PostMapping("/introspect")
    public ApiResponse<IntrospectResponse> introspect(@RequestBody IntrospectRequest request) throws JOSEException, ParseException {
        var authenticated = authentication.introspect(request);
        return ApiResponse.<IntrospectResponse>builder()
                .data(authenticated)
                .code(1000)
                .build();
    }

    @PostMapping("/logout")
    public ApiResponse<Void> logout(@RequestBody LogoutRequest request) throws JOSEException, ParseException {
        authentication.logout(request);
        return ApiResponse.<Void>builder()
                .build();
    }

}