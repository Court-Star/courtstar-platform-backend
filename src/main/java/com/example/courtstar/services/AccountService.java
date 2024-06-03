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
import com.example.courtstar.util.EmailUtil;
import com.example.courtstar.util.OtpUtil;
import jakarta.mail.MessagingException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@Slf4j
@EnableWebSecurity
@EnableMethodSecurity
public class AccountService {
     AccountReponsitory accountReponsitory;
     RoleReponsitory roleReponsitory;
     AccountMapper accountMapper;
     RoleService roleService;
    @Autowired
    PasswordEncoder passwordEncoder;
    private final OtpUtil otpUtil;
    private final EmailUtil emailUtil;

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

    public Account CreateManagerAccount(AccountCreationRequest request) {
        if(accountReponsitory.existsByEmail(request.getEmail())){
            throw new AppException(ErrorCode.ACCOUNT_EXIST);
        }
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        Account account = accountMapper.toAccount(request);
        account.setPassword(passwordEncoder.encode(request.getPassword()));
        Set<Role> roles = new HashSet<>();
        roles.add(roleReponsitory.findById("CUSTOMER").orElse(null));
        roles.add(roleReponsitory.findById("MANAGER").orElse(null));
        roles.add(roleReponsitory.findById("STAFF").orElse(null));
        account.setRoles(roles);
        return accountReponsitory.save(account);
    }

    public Account CreateStaffAccount(AccountCreationRequest request) {
        if(accountReponsitory.existsByEmail(request.getEmail())){
            throw new AppException(ErrorCode.ACCOUNT_EXIST);
        }
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        Account account = accountMapper.toAccount(request);
        account.setPassword(passwordEncoder.encode(request.getPassword()));
        Set<Role> roles = new HashSet<>();
        roles.add(roleReponsitory.findById("CUSTOMER").orElse(null));
        roles.add(roleReponsitory.findById("STAFF").orElse(null));
        account.setRoles(roles);
        return accountReponsitory.save(account);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<Account> getAllAccounts(){
        return accountReponsitory.findAll();
    }

    @PreAuthorize("hasAuthority('GET_ACCOUNT_BY_ID')")
    public AccountResponse getAccountById(int id){
        Account account = accountReponsitory.findById(id).orElseThrow(()->new AppException(ErrorCode.NOT_FOUND_USER));
        AccountResponse accountResponse = accountMapper.toAccountResponse(account);
        return accountResponse;
    }

    @PreAuthorize("hasRole('ADMIN')")
    public AccountResponse deleteAccountById(int id){
         if(accountReponsitory.existsById(id)){
             Account account = accountReponsitory.findById(id).orElseThrow(()->new AppException(ErrorCode.NOT_FOUND_USER));
             account.setDelete(true);
             account=accountReponsitory.save(account);
             return accountMapper.toAccountResponse(account);
         }else {
              throw new AppException(ErrorCode.NOT_DELETE);
         }

    }

    public AccountResponse UpdatePassword(String email,String newPassword){
        Account account = accountReponsitory.findByEmail(email).orElseThrow(()->new AppException(ErrorCode.NOT_FOUND_USER));
        account.setPassword(passwordEncoder.encode(newPassword));
        accountReponsitory.save(account);

        return accountMapper.toAccountResponse(account);
    }

    @PreAuthorize("hasAuthority('UPDATE_ACCOUNT')")
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
        //accountResponse.setRoles(account.getRole());
        return accountResponse;
    }

    public boolean checkExistEmail(String email){
        return accountReponsitory.existsByEmail(email);
    }

    @PreAuthorize("hasAuthority('GET_MY_INFO')")
    public  AccountResponse getMyAccount(){
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();
        Account account = accountReponsitory.findByEmail(name).orElseThrow(()->new AppException(ErrorCode.NOT_FOUND_USER));
        return accountMapper.toAccountResponse(account);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<AccountResponse> getAllAccountsBanned(){
         return accountReponsitory.findAllByIsDelete(true).stream().map(accountMapper::toAccountResponse).toList();
    }

    public String generateOtp(String email){
        System.out.println(email);
        Account account = accountReponsitory.findByEmail(email)
                .orElseThrow(()->new AppException(ErrorCode.NOT_FOUND_USER));
        String otp = otpUtil.generateOtp();
        try{
            emailUtil.sendOtpEmail(email,otp);
        } catch (MessagingException e) {
            throw new AppException(ErrorCode.OTP_ERROR);
        }

        account.setOtp(otp);
        account.setOtpGeneratedTime(LocalDateTime.now());
        accountReponsitory.save(account);
        return otp;
    }

    public boolean VerifyOtp(String email,String otp){
        Account account = accountReponsitory.findByEmail(email)
                .orElseThrow(()->new AppException(ErrorCode.NOT_FOUND_USER));
        if(!(otp.equals(account.getOtp())
                && Duration.between(account.getOtpGeneratedTime(), LocalDateTime.now()).toSeconds() < 60)){
            throw new AppException(ErrorCode.OTP_ERROR);
        }

        return true;
    }
}
