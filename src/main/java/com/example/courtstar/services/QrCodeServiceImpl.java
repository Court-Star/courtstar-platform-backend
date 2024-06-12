package com.example.courtstar.services;



import com.example.courtstar.dto.request.CheckInRequest;
import com.example.courtstar.dto.request.SendMailBookingRequest;
import com.example.courtstar.entity.Account;
import com.example.courtstar.entity.BookingSchedule;
import com.example.courtstar.entity.Guest;
import com.example.courtstar.repositories.GuestRepository;
import com.example.courtstar.util.AppUtils;
import com.example.courtstar.util.EmailBookingUtil;
import com.google.zxing.WriterException;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class QrCodeServiceImpl implements QrCodeService {
    @Autowired
    EmailBookingUtil emailBookingUtil;
    @Autowired
    AccountService accountService;
    @Autowired
    GuestRepository guestRepository;
    @Autowired
    BookingService bookingService;


    @Override
    public String generateQrCode(String  email) throws IOException, WriterException, MessagingException {
        Account account = accountService.getAccountByEmail1(email);
        System.out.println(account);
        SendMailBookingRequest request = null;
        int slot= 0;
        CheckInRequest checkInRequest = CheckInRequest.builder()
                .email(email)
                .build();
        if(account == null){
            Guest guest = guestRepository.findByEmail(email);
            BookingSchedule bookingSchedule= bookingService.getBookingBuyGuestID(guest.getId());
            request = SendMailBookingRequest.builder()
                    .email(email)
                    .firstName(guest.getFullName())
                    .lastName("")
                    .phone(guest.getPhone())
                    .number_Court(bookingSchedule.getCourt().getCourtNo())
                    .price(bookingSchedule.getTotalPrice())
                    .booking_id(bookingSchedule.getId())
                    .build();
            slot=bookingSchedule.getSlot().getId();
            checkInRequest.setSlot(slot);
            checkInRequest.setCourt(bookingSchedule.getCourt().getId());
        }else{
            BookingSchedule bookingSchedule= bookingService.getBookingSchedule(account.getId());
            request = SendMailBookingRequest.builder()
                    .email(email)
                    .firstName(account.getFirstName())
                    .lastName(account.getLastName())
                    .phone(account.getPhone())
                    .number_Court(bookingSchedule.getCourt().getCourtNo())
                    .price(bookingSchedule.getTotalPrice())
                    .booking_id(bookingSchedule.getId())
                    .build();
            slot=bookingSchedule.getSlot().getId();
            checkInRequest.setSlot(slot);
            checkInRequest.setCourt(bookingSchedule.getCourt().getId());

        }



        String prettyData = AppUtils.prettyObject(checkInRequest);
        String qrCode = AppUtils.generateQrCode(prettyData,300,300);
        return emailBookingUtil.sendORCode(qrCode,request);
    }
}
