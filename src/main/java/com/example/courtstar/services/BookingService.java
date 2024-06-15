package com.example.courtstar.services;

import com.example.courtstar.dto.request.BookingRequest;
import com.example.courtstar.dto.request.OrderRequest;
import com.example.courtstar.entity.*;
import com.example.courtstar.exception.AppException;
import com.example.courtstar.exception.ErrorCode;
import com.example.courtstar.repositories.*;
import com.example.courtstar.services.payment.CreateOrderService;
import com.google.zxing.WriterException;
import jakarta.mail.MessagingException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

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
    private CreateOrderService createOrderService;


    public Map<String, Object> booking(BookingRequest request) throws MessagingException, IOException, WriterException, JSONException {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();

        Account account = null;
        Guest guest = null;
        if (name.equals("anonymousUser")) {
            guest = guestRepository.findByEmail(name);
            if (guest == null) {
                guest = Guest.builder()
                        .email(request.getEmail())
                        .phone(request.getPhone())
                        .fullName(request.getFullName())
                        .build();
                guestRepository.save(guest);
            }
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


        slotUnavailableRepository.save(SlotUnavailable.builder()
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

        Payment payment = Payment.builder()
                .date(LocalDate.now())
                .status(true)
                .bookingSchedule(bookingSchedule)
                .build();

        paymentRepository.save(payment);

        System.out.println("================");
        System.out.println(centre.getManager().getId());
        System.out.println("================");



        OrderRequest orderRequest = OrderRequest.builder()
                .bookingSchedule(bookingSchedule)
                .centre(centre)
                .payment(payment)
                .build();

        //payment


        return createOrderService.createOrder(orderRequest);
    }

    public List<BookingSchedule> getBookingSchedules(int centreId) {
        return bookingScheduleRepository.findAllByCentreId(centreId);
    }

}
