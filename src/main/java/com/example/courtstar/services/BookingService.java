package com.example.courtstar.services;

import com.example.courtstar.dto.request.BookingRequest;
import com.example.courtstar.dto.request.CentreManagerRequest;
import com.example.courtstar.entity.*;
import com.example.courtstar.exception.AppException;
import com.example.courtstar.exception.ErrorCode;
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

import java.time.LocalDate;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@Slf4j
@EnableWebSecurity
@EnableMethodSecurity
public class BookingService {

    @Autowired
    AccountReponsitory accountReponsitory;

    @Autowired
    CourtRepository courtRepository;

    @Autowired
    SlotRepository slotRepository;

    @Autowired
    SlotUnavailableRepository slotUnavailableRepository;

    @Autowired
    BookingScheduleRepository bookingScheduleRepository;

    @Autowired
    PaymentRepository paymentRepository;

    public BookingSchedule booking(BookingRequest request) {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();
        Account account = accountReponsitory.findByEmail(name).orElseThrow(
                () -> new AppException(ErrorCode.NOT_FOUND_USER)
        );

        Slot slot = slotRepository.findById(request.getSlotId()).orElseThrow(null);
        Court court = courtRepository.findById(request.getCourtId()).orElseThrow(null);
        Centre centre = court.getCentre();

        SlotUnavailable slotUnavailable = SlotUnavailable.builder()
                .date(request.getDate())
                .court(court)
                .slot(slot)
                .build();

        slotUnavailableRepository.save(slotUnavailable);

        BookingSchedule bookingSchedule = bookingScheduleRepository.save(BookingSchedule.builder()
                .date(request.getDate())
                .totalPrice(centre.getPricePerHour())
                .status(false)
                .account(account)
                .slot(slot)
                .court(court)
                .build());

        Payment payment = Payment.builder()
                .date(LocalDate.now())
                .status(true)
                .paymentMethod("Paypal")
                .bookingSchedule(bookingSchedule)
                .build();

        paymentRepository.save(payment);

        return bookingSchedule;
    }
}
