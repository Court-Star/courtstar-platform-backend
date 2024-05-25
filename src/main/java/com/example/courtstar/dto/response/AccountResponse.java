package com.example.courtstar.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AccountResponse {
    String email;
    String phone;
    Integer roleId;
    String firstName;
    String lastName;
}
