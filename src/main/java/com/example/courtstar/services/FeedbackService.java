package com.example.courtstar.services;

import com.example.courtstar.dto.request.FeedbackRequest;
import com.example.courtstar.dto.response.FeedbackResponse;
import com.example.courtstar.entity.Account;
import com.example.courtstar.entity.Feedback;
import com.example.courtstar.exception.AppException;
import com.example.courtstar.exception.ErrorCode;
import com.example.courtstar.mapper.FeedbackMapper;
import com.example.courtstar.repositories.AccountReponsitory;
import com.example.courtstar.repositories.BookingScheduleRepository;
import com.example.courtstar.repositories.CentreRepository;
import com.example.courtstar.repositories.FeedbackRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@Slf4j
@EnableWebSecurity
@EnableMethodSecurity
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final FeedbackMapper feedbackMapper;
    private final AccountReponsitory accountReponsitory;
    private final CentreRepository centreRepository;
    private final BookingScheduleRepository bookingScheduleRepository;

    public Feedback createFeedback(FeedbackRequest request) {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();
        Account account = accountReponsitory.findByEmail(name).orElseThrow(
                ()->new AppException(ErrorCode.NOT_FOUND_USER)
        );

        Feedback feedback = feedbackMapper.toFeedback(request);
        feedback.setAccount(account);
        feedback.setCentre(centreRepository.findById(request.getCentreId())
                .orElseThrow(null));
        feedback.setBookingSchedule(bookingScheduleRepository.findById(request.getBookingId())
                .orElseThrow(null));

        return feedbackRepository.save(feedback);
    }

    public List<FeedbackResponse> getFeedbackOfCentre(int centreId) {
        List<Feedback> feedbackList = feedbackRepository.findAllByCentreId(centreId).orElseThrow(
                () -> new RuntimeException("No feedback found for centre " + centreId)
        );

        List<FeedbackResponse> feedbackResponses = feedbackList.stream().map(
                feedback -> {
                    FeedbackResponse feedbackResponse = feedbackMapper.toFeedbackResponse(feedback);
                    feedbackResponse.setFullName(feedback.getAccount().getFirstName()
                            + " " + feedback.getAccount().getLastName());
                    return feedbackResponse;
                }
        ).toList();
        return feedbackResponses;
    }
}
