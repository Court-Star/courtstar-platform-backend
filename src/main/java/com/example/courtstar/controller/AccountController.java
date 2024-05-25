package com.example.courtstar.controller;

import com.example.courtstar.dto.request.AccountCreationRequest;
import com.example.courtstar.dto.request.AccountUpdateRequest;
import com.example.courtstar.dto.request.ApiResponse;
import com.example.courtstar.dto.response.AccountResponse;
import com.example.courtstar.entity.Account;
import com.example.courtstar.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/Account")
public class AccountController {
    @Autowired
    private AccountService accountService;
    @PostMapping
    public ApiResponse<AccountResponse> createAccount(@RequestBody @Valid AccountCreationRequest request){
        ApiResponse apiResponse = ApiResponse.builder()
                .data(accountService.CreateAccount(request))
                .build();
        return apiResponse;
    }
    @GetMapping
    public ApiResponse<Account> getAccount(){
        ApiResponse apiResponse = ApiResponse.builder()
                .data(accountService.getAllAccounts())
                .build();

        return apiResponse;
    }
    @GetMapping("/{id}")
    public ApiResponse<AccountResponse> getAccountById(@PathVariable int id){
        return ApiResponse.<AccountResponse>builder()
                .data(accountService.getAccountById(id))
                .build();
    }
    @DeleteMapping("/{id}")
    public ApiResponse<Boolean> deleteAccountById(@PathVariable int id){
        return ApiResponse.<Boolean>builder()
                .data(accountService.deleteAccountById(id))
                .code(1000)
                .message("delete success")
                .build();
    }
    @PutMapping("/{id}")
    public ApiResponse<AccountResponse> updateAccountById(@PathVariable int id, @RequestBody @Valid AccountUpdateRequest request){
        return ApiResponse.<AccountResponse>builder()
                .data(accountService.updateAccount(id,request))
                .build();
    }
}
