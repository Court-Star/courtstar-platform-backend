package com.example.courtstar.services;

import com.example.courtstar.dto.request.CentreManagerRequest;
import com.example.courtstar.dto.request.CentreRequest;
import com.example.courtstar.dto.request.CourtRequest;
import com.example.courtstar.dto.response.AccountResponse;
import com.example.courtstar.dto.response.CentreResponse;
import com.example.courtstar.entity.*;
import com.example.courtstar.exception.AppException;
import com.example.courtstar.exception.ErrorCode;
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
    public CentreManager addInformation(CentreManagerRequest request) {
        CentreManager centreManager = centreManagerMapper.toCentreManager(request);
        centreManager.setCentres(new HashSet<Centre>());
        return centreManagerRepository.save(centreManager);
    }

    public CentreManager updateInformation(int id,CentreManagerRequest request){
        CentreManager manager = centreManagerRepository.findById(id).orElseThrow(null);
        Role role= manager.getAccount().getRoles().stream().filter(i->i.getName().equals("MANAGER")).findFirst().orElse(null);
        if(role==null){
            throw new AppException(ErrorCode.ERROR_ROLE);
        }
        centreManagerMapper.updateCentre(manager,request);
        return centreManagerRepository.save(manager);
    }

    public CentreManager addCentre(int id, CentreRequest request){
        CentreManager manager = centreManagerRepository.findById(id).orElseThrow(()-> new AppException(ErrorCode.NOT_FOUND_USER));
        Role role= manager.getAccount().getRoles().stream().filter(i->i.getName().equals("MANAGER")).findFirst().orElse(null);
        if(role==null){
            throw new RuntimeException();
        }
        Centre centre = centreRepository.save(centreMapper.toCentre(request));

        Set<Centre> centreSet = manager.getCentres();
        centreSet.add(centre);

        CourtRequest courtRequest = new CourtRequest();
        centre.setCourts(AddCourt(centre.getId(),courtRequest));

        Set<Image> images = centre.getImages();
        if(images==null){
            images = new HashSet<>();
        }
        images.addAll(request.getImages());
        centre.setImages(images);
        imgRepository.saveAll(images);
        centreRepository.save(centre);
        return centreManagerRepository.save(manager);
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
            setCentre.add(courtRepository.save(court));
        }
        centre.setCourts(setCentre);
        return centreRepository.save(centre).getCourts();
    }

}
