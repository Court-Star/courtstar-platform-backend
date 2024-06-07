package com.example.courtstar.services;

import com.example.courtstar.dto.request.CentreRequest;
import com.example.courtstar.dto.request.CourtRequest;
import com.example.courtstar.dto.response.CentreResponse;
import com.example.courtstar.dto.response.CourtResponse;
import com.example.courtstar.entity.Account;
import com.example.courtstar.entity.Centre;
import com.example.courtstar.entity.CentreManager;
import com.example.courtstar.entity.Court;
import com.example.courtstar.exception.AppException;
import com.example.courtstar.exception.ErrorCode;
import com.example.courtstar.mapper.CentreMapper;
import com.example.courtstar.mapper.CourtMapper;
import com.example.courtstar.repositories.AccountReponsitory;
import com.example.courtstar.repositories.CentreManagerRepository;
import com.example.courtstar.repositories.CentreRepository;
import com.example.courtstar.repositories.CourtRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@Slf4j
@EnableWebSecurity
@EnableMethodSecurity

public class CentreService {
    @Autowired
    CentreRepository centreRepository;
    @Autowired
    CentreMapper centreMapper;
    @Autowired
    AccountReponsitory accountReponsitory;
    @Autowired
    CourtRepository courtRepository;
    @Autowired
    CourtMapper courtMapper;
    @Autowired
    private CentreManagerRepository centreManagerRepository;

    public CentreResponse createCentre(CentreRequest request) {
        Centre centre = centreMapper.toCentre(request);
        return centreMapper.toCentreResponse(centreRepository.save(centre));
    }

    public CentreResponse UpdateCentre(int id,CentreRequest request) {
        Centre centre = centreRepository.findById(id).orElseThrow(()->new AppException(ErrorCode.NOT_FOUND_CENTRE));
      if(!centre.isStatus()){
           throw new AppException(ErrorCode.CENTRE_DISABLE);
        }
        centreMapper.updateCentre(centre,request);
        return  centreMapper.toCentreResponse(centreRepository.save(centre));
    }

    public List<CentreResponse> getAllCentres() {
        return centreRepository.findAll().stream().map(centreMapper::toCentreResponse).toList();
    }

    public List<CentreResponse> getAllCentresIsActive(String email,boolean isActive) {
        return centreRepository.findAllByNameAndStatus(email,isActive).stream().map(centreMapper::toCentreResponse).toList();
    }

    public CentreResponse getCentre(int id) {
        return centreMapper.toCentreResponse(centreRepository.findById(id)
                .orElseThrow(()->new AppException(ErrorCode.NOT_FOUND_CENTRE)));
    }


    public Set<CentreResponse> getAllCentresOfManager(String email){
        Account account = accountReponsitory.findByEmail(email).orElseThrow(()->new AppException(ErrorCode.NOT_FOUND_USER));
        CentreManager centreManager = centreManagerRepository.findByAccountId(account.getId()).orElseThrow(()->new AppException(ErrorCode.NOT_FOUND_USER));
        Set<CentreResponse> centreResponses = centreManager.getCentres().stream()
                .map(centre -> {
                    CentreResponse centreResponse = centreMapper.toCentreResponse(centre);
                    centreResponse.setManagerId(centreManager.getId());
                    return centreResponse;
                }).collect(Collectors.toSet());
        return centreResponses;
    }
    public CentreResponse isActive(int id, boolean active) {
        Centre centre = centreRepository.findById(id).orElseThrow(()->new AppException(ErrorCode.NOT_FOUND_CENTRE));
        System.out.println(active);
        centre.setStatus(active);
        Centre centre1= centreRepository.save(centre);
        System.out.println(centre1.isStatus());
        CentreResponse centreResponse = centreMapper.toCentreResponse(centre1);
        System.out.println(centreResponse.isStatus());
        return centreResponse;
    }
}
