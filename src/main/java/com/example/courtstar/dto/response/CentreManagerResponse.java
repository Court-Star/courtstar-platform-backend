package com.example.courtstar.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class CentreManagerResponse {
    String address;
    double currentBalance;
    double totalRevenue;
    double todayIncome;
    double percent;
    double pending;
    AccountResponse account;
}