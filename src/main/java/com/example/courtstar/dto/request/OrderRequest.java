package com.example.courtstar.dto.request;

import com.example.courtstar.entity.BookingSchedule;
import com.example.courtstar.entity.Centre;
import com.example.courtstar.entity.Payment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderRequest{
    private BookingSchedule bookingSchedule;
    private Payment payment;
    private Centre centre;
    private String callback_url="https://6361-2405-4802-90b4-f810-889f-9ed0-d264-650b.ngrok-free.app/courtstar/payment/callbackBooking";;

}
