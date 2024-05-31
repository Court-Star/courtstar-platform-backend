package com.example.courtstar.repositories;

import com.example.courtstar.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleReponsitory extends JpaRepository<Role, String> {
}
