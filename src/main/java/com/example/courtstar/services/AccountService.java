package com.example.courtstar.services;

import com.example.courtstar.dto.request.AccountCreationRequest;
import com.example.courtstar.dto.request.AccountUpdateRequest;
import com.example.courtstar.dto.response.AccountResponse;
import com.example.courtstar.entity.Account;
import com.example.courtstar.entity.Role;
import com.example.courtstar.exception.AppException;
import com.example.courtstar.exception.ErrorCode;
import com.example.courtstar.mapper.AccountMapper;
import com.example.courtstar.repositories.AccountReponsitory;
import com.example.courtstar.repositories.RoleReponsitory;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@Slf4j
public class AccountService {
     AccountReponsitory accountReponsitory;
     RoleReponsitory roleReponsitory;
     AccountMapper accountMapper;
     RoleService roleService;

    public Account CreateAccount(AccountCreationRequest request) {
        if(accountReponsitory.existsByEmail(request.getEmail())){
            throw new AppException(ErrorCode.ACCOUNT_EXIST);
        }
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        Account account = accountMapper.toAccount(request);
        account.setPassword(passwordEncoder.encode(request.getPassword()));
        var roles = roleService.findAllById("CUSTOMER");
        account.setRoles(new HashSet<>(roles));
        return accountReponsitory.save(account);
    }
    @Secured("hasRole('CUSTOMER')")
    public List<Account> getAllAccounts(){
        log.warn("notfound");
        return accountReponsitory.findAll();
    }
    public AccountResponse getAccountById(int id){
        Account account = accountReponsitory.findById(id).orElseThrow(()->new AppException(ErrorCode.NOT_FOUND_USER));
        AccountResponse accountResponse = accountMapper.toAccountResponse(account);
        return accountResponse;
    }
    public AccountResponse deleteAccountById(int id){
         if(accountReponsitory.existsById(id)){
             Account account = accountReponsitory.findById(id).orElseThrow(()->new AppException(ErrorCode.NOT_FOUND_USER));
             account.setIDelete(true);
             account=accountReponsitory.save(account);
             return accountMapper.toAccountResponse(account);
         }else {
              throw new AppException(ErrorCode.NOT_DELETE);
         }

    }

    public AccountResponse updateAccount(int id,AccountUpdateRequest request){
        Account account = accountReponsitory.findById(id).orElseThrow(()->new AppException(ErrorCode.NOT_FOUND_USER));
        accountMapper.updateAccount(account,request);

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        account.setPassword(passwordEncoder.encode(request.getPassword()));
        var roles = roleReponsitory.findAllById(request.getRoles());
        account.setRoles(new HashSet<>(roles));
        AccountResponse accountResponse = accountMapper.toAccountResponse(accountReponsitory.save(account));
        return accountResponse;
    }

    public AccountResponse getAccountByEmail(String email){
        Account account = accountReponsitory.findByEmail(email)
                .orElseThrow(()->new AppException(ErrorCode.NOT_FOUND_USER));
        AccountResponse accountResponse =accountMapper.toAccountResponse(account);
        //accountResponse.setRoles(account.getRole());
        return accountResponse;
    }
    public boolean checkExistEmail(String email){
        return accountReponsitory.existsByEmail(email);
    }

    public  AccountResponse getMyAccount(){
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();
        Account account = accountReponsitory.findByEmail(name).orElseThrow(()->new AppException(ErrorCode.NOT_FOUND_USER));
        return accountMapper.toAccountResponse(account);
    }

    public List<AccountResponse> getAllAccountsBanned(){
         return accountReponsitory.findAllByIDelete(true).stream().map(accountMapper::toAccountResponse).toList();
    }
}
