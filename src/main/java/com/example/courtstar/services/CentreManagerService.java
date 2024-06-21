package com.example.courtstar.services;

import com.example.courtstar.dto.request.CentreManagerRequest;
import com.example.courtstar.dto.response.AccountResponse;
import com.example.courtstar.dto.response.CentreManagerResponse;
import com.example.courtstar.entity.*;
import com.example.courtstar.exception.AppException;
import com.example.courtstar.exception.ErrorCode;
import com.example.courtstar.mapper.AccountMapper;
import com.example.courtstar.mapper.CentreManagerMapper;
import com.example.courtstar.repositories.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@Slf4j
@EnableWebSecurity
@EnableMethodSecurity
public class CentreManagerService {
    @Autowired
    CentreManagerRepository centreManagerRepository;
    @Autowired
    CentreManagerMapper centreManagerMapper;
    @Autowired
    AccountReponsitory accountReponsitory;
    @Autowired
    private AccountMapper accountMapper;

    public List<CentreManagerResponse> getAllManager() {
        List<CentreManager> managers = centreManagerRepository.findAll();
        return managers.stream()
                .map(
                        manager -> {
                            AccountResponse accountResponse = accountMapper.toAccountResponse(manager.getAccount());
                            return CentreManagerResponse.builder()
                                    .account(accountResponse)
                                    .address(manager.getAddress())
                                    .currentBalance(manager.getCurrentBalance())
                                    .build();
                        }
                ).toList();
    }

    public CentreManagerResponse getManagerInfo() {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();
        Account account = accountReponsitory.findByEmail(name).orElseThrow(()->new AppException(ErrorCode.NOT_FOUND_USER));
        CentreManager manager = centreManagerRepository.findByAccountId(account.getId()).orElseThrow(null);
        AccountResponse accountResponse = accountMapper.toAccountResponse(account);
        return CentreManagerResponse.builder()
                .account(accountResponse)
                .address(manager.getAddress())
                .currentBalance(manager.getCurrentBalance())
                .build();
    }

    public CentreManager addInformation(CentreManagerRequest request) {
        CentreManager centreManager = centreManagerMapper.toCentreManager(request);
        centreManager.setCentres(new ArrayList<>());
        return centreManagerRepository.save(centreManager);
    }

    public CentreManagerResponse updateInformation(int account_id, CentreManagerRequest request){
        CentreManager manager = centreManagerRepository.findByAccountId(account_id).orElseThrow(null);
        String role= manager.getAccount().getRole().getName();
        if(!role.equals("MANAGER")){
            throw new AppException(ErrorCode.ERROR_ROLE);
        }
        centreManagerMapper.updateCentre(manager,request);
        centreManagerRepository.save(manager);
        AccountResponse accountResponse = accountMapper.toAccountResponse(manager.getAccount());

        return CentreManagerResponse.builder()
                .account(accountResponse)
                .address(manager.getAddress())
                .currentBalance(manager.getCurrentBalance())
                .build();
    }
}
