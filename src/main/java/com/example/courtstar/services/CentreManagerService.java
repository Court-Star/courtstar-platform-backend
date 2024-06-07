package com.example.courtstar.services;

import com.example.courtstar.dto.request.CentreManagerRequest;
import com.example.courtstar.dto.request.CentreRequest;
import com.example.courtstar.dto.request.CourtRequest;
import com.example.courtstar.dto.response.AccountResponse;
import com.example.courtstar.dto.response.CentreManagerResponse;
import com.example.courtstar.dto.response.CentreResponse;
import com.example.courtstar.entity.*;
import com.example.courtstar.exception.AppException;
import com.example.courtstar.exception.ErrorCode;
import com.example.courtstar.mapper.AccountMapper;
import com.example.courtstar.mapper.CentreManagerMapper;
import com.example.courtstar.mapper.CentreMapper;
import com.example.courtstar.mapper.CourtMapper;
import com.example.courtstar.repositories.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

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
    CentreRepository centreRepository;
    @Autowired
    CentreMapper centreMapper;
    @Autowired
    CourtRepository courtRepository;
    @Autowired
    CourtMapper courtMapper;
    @Autowired
    ImgRepository imgRepository;
    @Autowired
    private AccountMapper accountMapper;

    public CentreManager addInformation(CentreManagerRequest request) {
        CentreManager centreManager = centreManagerMapper.toCentreManager(request);
        centreManager.setCentres(new HashSet<Centre>());
        return centreManagerRepository.save(centreManager);
    }

    public CentreManagerResponse updateInformation(int account_id, CentreManagerRequest request){
        CentreManager manager = centreManagerRepository.findByAccountId(account_id).orElseThrow(null);
        Role role= manager.getAccount().getRoles().stream().filter(i->i.getName().equals("MANAGER")).findFirst().orElse(null);
        if(role==null){
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

    public CentreResponse addCentre(int account_id, CentreRequest request){
        CentreManager manager = centreManagerRepository.findByAccountId(account_id)
                .orElseThrow( () -> new AppException(ErrorCode.NOT_FOUND_USER));
        Role role= manager.getAccount().getRoles().stream()
                .filter( i -> i.getName().equals("MANAGER"))
                .findFirst()
                .orElse(null);
        if(role==null){
            throw new RuntimeException();
        }
        Centre centre = centreMapper.toCentre(request);

        Set<Image> imgList = request.getImages().stream().map(url -> {
            Image image = new Image();
            image.setUrl(url);
            image.setCentre(centre);
            return image;
        }).collect(Collectors.toSet());

        centre.setImages(imgList);
        centre.setManager(manager);
        centreRepository.save(centre);

        Set<Centre> centreSet = manager.getCentres();
        if (centreSet == null) {
            centreSet = new HashSet<>();
        }
        centreSet.add(centre);
        manager.setCentres(centreSet);

        CourtRequest courtRequest = new CourtRequest();
        centre.setCourts(AddCourt(centre.getId(),courtRequest));

        imgRepository.saveAll(imgList);
        centreRepository.save(centre);
        centreManagerRepository.save(manager);

        CentreResponse centreResponse = centreMapper.toCentreResponse(centre);
        centreResponse.setManagerId(manager.getId());
        return centreResponse;
    }

    private Set<Court> AddCourt(int idCentre, CourtRequest request){
        Centre centre = centreRepository.findById(idCentre).orElseThrow(()->new AppException(ErrorCode.NOT_FOUND_CENTRE));
        var setCentre = centre.getCourts();
        if(setCentre==null){
            setCentre= new HashSet<>();
        }

        for(int i=0;i<centre.getNumberOfCourt();i++){
            Court court = courtMapper.toCourt(request.builder()
                    .courtNo(i+1)
                    .status(true)
                    .build());
            court.setCentre(centre);
            setCentre.add(courtRepository.save(court));
        }
        centre.setCourts(setCentre);
        return centreRepository.save(centre).getCourts();
    }

}
