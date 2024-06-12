package com.example.courtstar.services;


import com.example.courtstar.dto.request.SendMailBookingRequest;
import com.google.zxing.WriterException;
import jakarta.mail.MessagingException;

import java.io.IOException;

public interface QrCodeService {
    String generateQrCode(int bookingScheduleId) throws IOException, WriterException, MessagingException;
}
