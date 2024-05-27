package com.example.courtstar.reponsitory;

import com.example.courtstar.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountReponsitory extends JpaRepository<Account, Integer> {
    Optional<Account> findByEmail(String email);
    boolean existsByEmail(String email);
    Account findByEmailAndPassword(String email, String password);
}
