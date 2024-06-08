package com.example.courtstar.repositories;

import com.example.courtstar.entity.BookingSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingScheduleRepository extends JpaRepository<BookingSchedule, Integer> {
}
