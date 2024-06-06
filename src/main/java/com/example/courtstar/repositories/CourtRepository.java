package com.example.courtstar.repositories;

import com.example.courtstar.entity.Centre;
import com.example.courtstar.entity.Court;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourtRepository extends JpaRepository<Court, Integer> {

}
