package com.example.courtstar.services;


import com.example.courtstar.dto.request.SendMailBookingRequest;
import com.google.zxing.WriterException;
import jakarta.mail.MessagingException;

import java.io.IOException;

public interface QrCodeService {
    String generateQrCode(String email) throws IOException, WriterException, MessagingException;
}
