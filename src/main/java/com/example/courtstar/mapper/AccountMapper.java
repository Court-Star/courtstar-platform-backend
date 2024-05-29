package com.example.courtstar.mapper;

import com.example.courtstar.dto.request.AccountCreationRequest;
import com.example.courtstar.dto.request.AccountUpdateRequest;
import com.example.courtstar.dto.response.AccountResponse;
import com.example.courtstar.entity.Account;
import com.example.courtstar.enums.Role;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class AccountMapper {

    public Account toAccount(AccountCreationRequest request) {
        if (request == null) {
            return null;
        }

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);

        Account account = Account.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .phone(request.getPhone())
                .role(Role.CUSTOMER.getValue())
                .build();

        return account;
    }

    public AccountResponse toAccountResponse(Account account) {
        if (account == null) {
            return null;
        }

        AccountResponse response = AccountResponse.builder()
                .email(account.getEmail())
                .firstName(account.getFirstName())
                .lastName(account.getLastName())
                .phone(account.getPhone())
                .role(Role.fromValue(account.getRole()).toString())
                .build();

        return response;
    }

    public void updateAccount(Account account, AccountUpdateRequest request) {
        if (account == null || request == null) {
            return;
        }

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);

        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            account.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        if (request.getPhone() != null && !request.getPhone().isEmpty()) {
            account.setPhone(request.getPhone());
        }

        if (request.getFirstName() != null && !request.getFirstName().isEmpty()) {
            account.setFirstName(request.getFirstName());
        }

        if (request.getLastName() != null && !request.getLastName().isEmpty()) {
            account.setLastName(request.getLastName());
        }
    }

}

