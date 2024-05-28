package com.example.courtstar.mapper;

import com.example.courtstar.dto.request.AccountCreationRequest;
import com.example.courtstar.dto.request.AccountUpdateRequest;
import com.example.courtstar.dto.response.AccountResponse;
import com.example.courtstar.entity.Account;

public class AccountMapper {

    public Account toAccount(AccountCreationRequest request) {
        if (request == null) {
            return null;
        }

        Account account = Account.builder()
                .email(request.getEmail())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .phone(request.getPhone())
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
                .build();

        return response;
    }

    public void updateAccount(Account account, AccountUpdateRequest request) {
        if (account == null || request == null) {
            return;
        }

        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            account.setPassword(request.getPassword());
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

