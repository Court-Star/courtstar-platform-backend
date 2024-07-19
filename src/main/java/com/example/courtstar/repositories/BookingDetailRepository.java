package com.example.courtstar.repositories;

import com.example.courtstar.entity.BookingDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface BookingDetailRepository extends JpaRepository<BookingDetail, Integer> {
    Optional<BookingDetail> findByDateAndCourtIdAndSlotId(LocalDate date, Integer courtId, Integer slotId);
}
