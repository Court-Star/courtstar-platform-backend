package com.example.courtstar.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class FeedbackRequest {
    String content;
    LocalDateTime createDate;
    int rate;
    int centreId;
}
