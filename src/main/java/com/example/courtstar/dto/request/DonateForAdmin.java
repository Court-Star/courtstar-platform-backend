package com.example.courtstar.dto.request;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class DonateForAdmin {
    private String amount;

    @Builder.Default
    private String callbackUrl="https://616a-2402-800-63b8-9e93-5c65-832c-299f-8633.ngrok-free.app/courtstar/payment/donate-callback";
}
