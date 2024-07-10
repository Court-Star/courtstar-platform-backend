package com.example.courtstar.services;

import com.example.courtstar.dto.request.BookingRequest;
import com.example.courtstar.entity.Court;
import com.example.courtstar.entity.Slot;
import com.example.courtstar.entity.SlotUnavailable;
import com.example.courtstar.repositories.CourtRepository;
import com.example.courtstar.repositories.SlotRepository;
import com.example.courtstar.repositories.SlotUnavailableRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@EnableWebSecurity
@EnableMethodSecurity
public class SlotService {

    private final SlotUnavailableRepository slotUnavailableRepository;
    private final SlotRepository slotRepository;
    private final CourtRepository courtRepository;

    public Boolean disableSlot(BookingRequest request) {

        List<Integer> slotIds = request.getSlotIds();
        List<Slot> slots = slotIds.stream().map(
                slotId -> slotRepository.findById(slotId).orElseThrow(null)
        ).toList();
        List<Court> courts = courtRepository.findAllByCourtNo(request.getCourtNo());

        Court court = courts.stream()
                .filter(c -> c.getCentre().getId().equals(request.getCentreId()))
                .findFirst()
                .orElseThrow(null);

        slots.forEach(
                slot -> {
                    slotUnavailableRepository.save(SlotUnavailable.builder()
                            .date(request.getDate())
                            .court(court)
                            .slot(slot)
                            .build());
                }
        );

        return true;
    }
}
