package com.example.courtstar.services;

import com.example.courtstar.entity.Account;
import com.example.courtstar.entity.CentreManager;
import com.example.courtstar.entity.TopUp;
import com.example.courtstar.entity.TransferMoney;
import com.example.courtstar.exception.AppException;
import com.example.courtstar.exception.ErrorCode;
import com.example.courtstar.repositories.AccountReponsitory;
import com.example.courtstar.repositories.CentreManagerRepository;
import com.example.courtstar.repositories.TopUpRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@Slf4j
@EnableWebSecurity
@EnableMethodSecurity
public class TopUpService {

    @Autowired
    private TopUpRepository topUpRepository;
    @Autowired
    private AccountReponsitory accountReponsitory;
    @Autowired
    private CentreManagerRepository centreManagerRepository;

    public List<TopUp> getAllTopUpOfManager() {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();

        Account account = accountReponsitory.findByEmail(name)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND_USER));

        CentreManager manager = centreManagerRepository.findByAccountId(account.getId())
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND_USER));
        return  topUpRepository.findAllByManagerId(manager.getId());
    }
}
