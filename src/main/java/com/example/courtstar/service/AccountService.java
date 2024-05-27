package com.example.courtstar.service;

import com.example.courtstar.dto.request.AccountCreationRequest;
import com.example.courtstar.dto.request.AccountUpdateRequest;
import com.example.courtstar.dto.response.AccountResponse;
import com.example.courtstar.entity.Account;
import com.example.courtstar.enums.Role;
import com.example.courtstar.exception.AppException;
import com.example.courtstar.exception.ErrorCode;
import com.example.courtstar.mapper.AccountMapper;
import com.example.courtstar.reponsitory.AccountReponsitory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
public class AccountService {
    @Autowired
    private AccountReponsitory accountReponsitory;
    @Autowired
    private AccountMapper accountMapper;
    public Account CreateAccount(AccountCreationRequest request) {
        if(accountReponsitory.existsByEmail(request.getEmail())){
            throw new AppException(ErrorCode.ACCOUNT_EXIST);
        }
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        Account account = accountMapper.toAccount(request);
        account.setPassword(passwordEncoder.encode(request.getPassword()));
        HashSet<String> roles = new HashSet<>();
        roles.add(Role.CUSTOMER.name());
        account.setRole(roles);
        return accountReponsitory.save(account);
    }
    public List<Account> getAllAccounts(){
        return accountReponsitory.findAll();
    }
    public AccountResponse getAccountById(int id){
        Account account = accountReponsitory.findById(id).orElseThrow(()->new AppException(ErrorCode.NOT_FOUND_USER));
        AccountResponse accountResponse = accountMapper.toAccountResponse(account);
        return accountResponse;
    }
    public boolean deleteAccountById(int id){
        boolean check = false;
        if(accountReponsitory.existsById(id)){
            accountReponsitory.deleteById(id);
            check = true;
        }
        return check;
    }
    public AccountResponse updateAccount(int id,AccountUpdateRequest request){
        Account account = accountReponsitory.findById(id).orElseThrow(()->new AppException(ErrorCode.NOT_FOUND_USER));
        accountMapper.updateAccount(account,request);
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        account.setPassword(passwordEncoder.encode(request.getPassword()));


        AccountResponse accountResponse = accountMapper.toAccountResponse(accountReponsitory.save(account));
        return accountResponse;
    }
    public AccountResponse getAccountByEmail(String email){
        Account account = accountReponsitory.findByEmail(email)
                .orElseThrow(()->new AppException(ErrorCode.NOT_FOUND_USER));
        AccountResponse accountResponse =accountMapper.toAccountResponse(account);
        accountResponse.setRoles(account.getRole());
        return accountResponse;
    }
    public Account findAccountByEmail(String email){
        return accountReponsitory.findByEmailAndPassword(email,"1");
    }
    public boolean checkExistEmail(String email){
        return accountReponsitory.existsByEmail(email);
    }
}
