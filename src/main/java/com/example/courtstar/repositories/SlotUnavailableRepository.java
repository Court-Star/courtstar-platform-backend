package com.example.courtstar.repositories;

import com.example.courtstar.entity.SlotUnavailable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface SlotUnavailableRepository extends JpaRepository<SlotUnavailable, Integer> {

    Optional<SlotUnavailable> findByDateAndCourtIdAndSlotId(LocalDate date, int courtId, int slotId);
}
