package com.example.courtstar.dto.request;

import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class AccountUpdateRequest {
    @Size(min = 6,message = "PASSWORD_INVALID")
    String password;
    @Size(min=10,max = 10,message = "PHONE_INVALID")
    String phone;
    String firstName;
    String lastName;
}
