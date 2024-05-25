package com.example.courtstar.reponsitory;

import com.example.courtstar.entity.Account;
import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserReponsitory extends JpaRepository<Account, Integer> {
    public Account findByEmail(String email);
    boolean existsByEmail(String email);
}
