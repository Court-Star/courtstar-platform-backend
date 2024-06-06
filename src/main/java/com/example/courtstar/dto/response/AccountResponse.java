package com.example.courtstar.dto.response;

import com.example.courtstar.entity.CentreManager;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AccountResponse {
    String email;
    String phone;
    String firstName;
    String lastName;
    Set<RoleResponse> roles;
    CentreManager centreManager;
    boolean IDelete;

}
