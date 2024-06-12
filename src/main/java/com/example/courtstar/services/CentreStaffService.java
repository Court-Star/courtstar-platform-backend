package com.example.courtstar.services;

import com.example.courtstar.entity.CentreStaff;
import com.example.courtstar.repositories.CentreStaffRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@Slf4j
@EnableWebSecurity
@EnableMethodSecurity
public class CentreStaffService {

    @Autowired
    private CentreStaffRepository centreStaffRepository;

    public List<CentreStaff> getCentreStaffOfCentre(int centreId) {
        return centreStaffRepository.findAllByCentreId(centreId);
    }
}
