package com.example.courtstar.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {
    private String fullName;
    private Long amount;
    private String description;
    private String email;
    private String phoneNumber;
}
