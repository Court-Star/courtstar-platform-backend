package com.example.courtstar.services;

import com.example.courtstar.dto.request.BookingRequest;
import com.example.courtstar.dto.request.CentreManagerRequest;
import com.example.courtstar.entity.*;
import com.example.courtstar.exception.AppException;
import com.example.courtstar.exception.ErrorCode;
import com.example.courtstar.repositories.*;
import com.google.zxing.WriterException;
import jakarta.mail.MessagingException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@Slf4j
@EnableWebSecurity
@EnableMethodSecurity
public class BookingService {

    @Autowired
    private AccountReponsitory accountReponsitory;

    @Autowired
    private CourtRepository courtRepository;

    @Autowired
    private SlotRepository slotRepository;

    @Autowired
    private SlotUnavailableRepository slotUnavailableRepository;

    @Autowired
    private BookingScheduleRepository bookingScheduleRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private GuestRepository guestRepository;

    @Autowired
    private CentreRepository centreRepository;

    @Autowired
    QrCodeService qrCodeService;

    public BookingSchedule booking(BookingRequest request) throws MessagingException, IOException, WriterException {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();

        Account account = null;
        Guest guest = null;
        if (name.equals("anonymousUser")) {
            guest = Guest.builder()
                    .email(request.getEmail())
                    .phone(request.getPhone())
                    .fullName(request.getFullName())
                    .build();
            guestRepository.save(guest);
        } else {
            account = accountReponsitory.findByEmail(name).orElseThrow(
                    () -> new AppException(ErrorCode.NOT_FOUND_USER)
            );
        }

        Centre centre = centreRepository.findById(request.getCentreId()).orElseThrow(null);
        Slot slot = slotRepository.findById(request.getSlotId()).orElseThrow(null);
        List<Court> courts = courtRepository.findAllByCourtNo(request.getCourtNo());

        Court court = courts.stream()
                .filter(c -> c.getCentre().getId().equals(request.getCentreId()))
                .findFirst()
                .orElseThrow(null);


        SlotUnavailable slotUnavailable = slotUnavailableRepository.save(SlotUnavailable.builder()
                .date(request.getDate())
                .court(court)
                .slot(slot)
                .build());

        BookingSchedule bookingSchedule = bookingScheduleRepository.save(BookingSchedule.builder()
                .date(request.getDate())
                .totalPrice(centre.getPricePerHour())
                .status(false)
                .account(account)
                .guest(guest)
                .slot(slot)
                .court(court)
                .build());

        List<BookingSchedule> slotBookingSchedules = slot.getBookingSchedules();
        if (slotBookingSchedules == null) {
            slotBookingSchedules = new ArrayList<>();
            slot.setBookingSchedules(slotBookingSchedules);
        }
        slotBookingSchedules.add(bookingSchedule);
        List<SlotUnavailable> slotUnavailables = slot.getSlotUnavailables();
        if (slotUnavailables == null) {
            slotUnavailables = new ArrayList<>();
            slot.setSlotUnavailables(slotUnavailables);
        }
        slotUnavailables.add(slotUnavailable);
        slotRepository.save(slot);


        List<BookingSchedule> courtBookingSchedules = court.getBookingSchedules();
        if (courtBookingSchedules == null) {
            courtBookingSchedules = new ArrayList<>();
            court.setBookingSchedules(courtBookingSchedules);
        }
        courtBookingSchedules.add(bookingSchedule);
        List<SlotUnavailable> courtSlotUnavailables = court.getSlotUnavailables();
        if (courtSlotUnavailables == null) {
            courtSlotUnavailables = new ArrayList<>();
            court.setSlotUnavailables(courtSlotUnavailables);
        }
        courtSlotUnavailables.add(slotUnavailable);
        courtRepository.save(court);

        if (name.equals("anonymousUser")) {
            List<BookingSchedule> gueBookingSchedules = guest.getBookingSchedules();
            if (gueBookingSchedules == null) {
                gueBookingSchedules = new ArrayList<>();
                guest.setBookingSchedules(gueBookingSchedules);
            }
            gueBookingSchedules.add(bookingSchedule);
            guestRepository.save(guest);
        } else {
            List<BookingSchedule> accBookingSchedules = account.getBookingSchedules();
            if (accBookingSchedules == null) {
                accBookingSchedules = new ArrayList<>();
                account.setBookingSchedules(accBookingSchedules);
            }
            accBookingSchedules.add(bookingSchedule);
            accountReponsitory.save(account);
        }

        Payment payment = Payment.builder()
                .date(LocalDate.now())
                .status(true)
                .paymentMethod("Paypal")
                .bookingSchedule(bookingSchedule)
                .build();

        paymentRepository.save(payment);
        qrCodeService.generateQrCode(bookingSchedule.getId());

        return bookingSchedule;
    }

    public List<BookingSchedule> getBookingSchedules(int centreId) {
        return bookingScheduleRepository.findAllByCentreId(centreId);
    }

}
