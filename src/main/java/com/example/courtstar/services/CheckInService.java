package com.example.courtstar.services;

import com.example.courtstar.entity.BookingSchedule;
import com.example.courtstar.repositories.BookingScheduleRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@Slf4j
@EnableWebSecurity
@EnableMethodSecurity
public class CheckInService {

    private final BookingScheduleRepository bookingScheduleRepository;

    public Boolean checkIn(int bookingScheduleId) {
        boolean result = false;
        BookingSchedule bookingSchedule = bookingScheduleRepository.findById(bookingScheduleId).orElse(null);
        if (bookingSchedule != null) {
            bookingSchedule.setStatus(true);
            bookingScheduleRepository.save(bookingSchedule);
            result = true;
        }
        return result;
    }

    public Boolean undoCheckIn(int bookingScheduleId) {
        boolean result = false;
        BookingSchedule bookingSchedule = bookingScheduleRepository.findById(bookingScheduleId).orElse(null);
        if (bookingSchedule != null) {
            bookingSchedule.setStatus(false);
            bookingScheduleRepository.save(bookingSchedule);
            result = true;
        }
        return result;
    }
}
