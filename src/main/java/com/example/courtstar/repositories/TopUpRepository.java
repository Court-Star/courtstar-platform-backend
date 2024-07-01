package com.example.courtstar.repositories;

import com.example.courtstar.entity.TopUp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TopUpRepository extends JpaRepository<TopUp, Integer> {
    List<TopUp> findAllByManagerId(Integer managerId);
}
