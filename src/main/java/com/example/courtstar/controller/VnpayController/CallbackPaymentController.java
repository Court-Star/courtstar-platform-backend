package com.example.courtstar.controller.VnpayController;

import com.example.courtstar.entity.BookingSchedule;
import com.example.courtstar.entity.Centre;
import com.example.courtstar.entity.Payment;
import com.example.courtstar.exception.AppException;
import com.example.courtstar.exception.ErrorCode;
import com.example.courtstar.repositories.*;
import com.example.courtstar.services.QrCodeService;
import com.google.zxing.WriterException;
import jakarta.mail.MessagingException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

@RestController
@RequestMapping("/payment-vn-pay")

public class CallbackPaymentController {
    @Autowired
    private QrCodeService qrCodeService;
    @Autowired
    private CentreRepository centreRepository;
    @Autowired
    private CentreManagerRepository centreManagerRepository;


    private Logger logger = Logger.getLogger(this.getClass().getName());
    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private BookingScheduleRepository bookingScheduleRepository;

    @GetMapping("/callback")
    public ResponseEntity<Map<String, Object>> doCallBack(@RequestParam Map<String, Object> callBackInfo) throws JSONException, MessagingException, IOException, WriterException {
        System.out.println(callBackInfo);
        String vnp_ResponseCode = (String) callBackInfo.get("vnp_ResponseCode");
        if("00".equals(vnp_ResponseCode)){
            String dataStr=(String) callBackInfo.get("vnp_OrderInfo");
            System.out.println(dataStr);
            JSONArray jsonArray = new JSONArray(dataStr);
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            System.out.println(jsonObject);
            int bookingId = jsonObject.getInt("bookingId");
            int paymentId = jsonObject.getInt("paymentId");
            int centreId = jsonObject.getInt("centreId");
            Payment payment = paymentRepository.findById(paymentId).orElseThrow(null);
            payment.setStatus(true);
            payment.setZpTransId((String) callBackInfo.get("vnp_TransactionStatus"));
            paymentRepository.save(payment);

            BookingSchedule bookingSchedule = bookingScheduleRepository.findById(bookingId).orElseThrow(null);
            bookingSchedule.setSuccess(true);
            bookingSchedule.getBookingDetails()
                    .forEach(detail -> detail.setStatus(true));
            bookingScheduleRepository.save(bookingSchedule);

            qrCodeService.generateQrCode(bookingId, payment.getTransactionCode());


            Centre centre =centreRepository.findById(centreId).orElseThrow(()->new AppException(ErrorCode.NOT_FOUND_CENTRE));
            Double payMoney = centre.getRevenue()+(long)callBackInfo.get("vnp_Amount");
            centre.setRevenue(payMoney);
            centreRepository.save(centre);

            Double totalPayMoney = centre.getManager().getCurrentBalance()+(long)callBackInfo.get("vnp_Amount")*0.95;
            centre.getManager().setCurrentBalance(totalPayMoney);
            centreManagerRepository.save(centre.getManager());
            // them duong dan return den

        }else {
            System.out.println("hi");

            // them duong dan return den
        }

        return new ResponseEntity<>(new HashMap<>(), HttpStatus.OK);
    }
}
