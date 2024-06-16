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
    private Long amount;
    private String description;
    private String id_manager_centre;
    private String callback_url="https://6361-2405-4802-90b4-f810-889f-9ed0-d264-650b.ngrok-free.app/courtstar/payment/callbackDonate";
}
