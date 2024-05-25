package com.example.courtstar.service;

import com.example.courtstar.dto.request.AccountCreationRequest;
import com.example.courtstar.dto.request.AccountUpdateRequest;
import com.example.courtstar.dto.response.AccountResponse;
import com.example.courtstar.entity.Account;
import com.example.courtstar.exception.AppException;
import com.example.courtstar.exception.ErrorCode;
import com.example.courtstar.mapper.AccountMapper;
import com.example.courtstar.reponsitory.UserReponsitory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService {
    @Autowired
    private UserReponsitory userReponsitory;
    @Autowired
    private AccountMapper accountMapper;
    public Account CreateAccount(AccountCreationRequest request) {
        if(userReponsitory.existsByEmail(request.getEmail())){
            throw new AppException(ErrorCode.ACCOUNT_EXIST);
        }
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        Account account = accountMapper.toAccount(request);
        System.out.println(passwordEncoder.encode(request.getPassword()));
        account.setPassword(passwordEncoder.encode(request.getPassword()));
        return userReponsitory.save(account);
    }
    public List<Account> getAllAccounts(){
        return userReponsitory.findAll();
    }
    public AccountResponse getAccountById(int id){
        Account account = userReponsitory.findById(id).orElseThrow(()->new AppException(ErrorCode.NOT_FOUND_USER));
        AccountResponse accountResponse = accountMapper.toAccountResponse(account);
        return accountResponse;
    }
    public boolean deleteAccountById(int id){
        boolean check = false;
        if(userReponsitory.existsById(id)){
            userReponsitory.deleteById(id);
            check = true;
        }
        return check;
    }
    public AccountResponse updateAccount(int id,AccountUpdateRequest request){
        Account account = userReponsitory.findById(id).orElseThrow(()->new AppException(ErrorCode.NOT_FOUND_USER));
        accountMapper.updateAccount(account,request);

        AccountResponse accountResponse = accountMapper.toAccountResponse( userReponsitory.save(account));
        return accountResponse;
    }
}
