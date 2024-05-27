package com.example.courtstar.controller;

import com.example.courtstar.dto.request.AccountCreationRequest;
import com.example.courtstar.dto.request.AccountUpdateRequest;
import com.example.courtstar.dto.request.ApiResponse;
import com.example.courtstar.dto.response.AccountResponse;
import com.example.courtstar.entity.Account;
import com.example.courtstar.exception.AppException;
import com.example.courtstar.exception.ErrorCode;
import com.example.courtstar.mapper.AccountMapper;
import com.example.courtstar.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/account")
public class AccountController {
    @Autowired
    private AccountService accountService;
    @Autowired
    private AccountMapper accountMapper;

    @PostMapping
    public ApiResponse<AccountResponse> createAccount(@RequestBody @Valid AccountCreationRequest request){
        ApiResponse apiResponse = ApiResponse.builder()
                .data(accountService.CreateAccount(request))
                .build();
        return apiResponse;
    }

    @GetMapping("/createEmail")
    public ApiResponse<AccountResponse> createAccountByGmail(@AuthenticationPrincipal OAuth2User principal){
        Map<String, Object> attributes = principal.getAttributes();
        String email= attributes.get("email").toString();
        ApiResponse apiResponse;
        boolean check = accountService.checkExistEmail(email);


        if(!check){
            String Name = (String) attributes.get("name");
            String[] fullName = Name.split(" ");

            apiResponse = ApiResponse.<AccountResponse>builder()
                    .data(accountMapper.toAccountResponse(
                            accountService.CreateAccount(
                                    AccountCreationRequest.builder()
                                            .email(email)
                                            .password("1")
                                            .firstName(fullName[0])
                                            .lastName(fullName[fullName.length-1])
                                            .build()
                            )
                    ))
                    .build();

        }
        AccountResponse account = accountService.getAccountByEmail(email);
        apiResponse = ApiResponse.<AccountResponse>builder()
                .code(1000)
                .data(account)
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
