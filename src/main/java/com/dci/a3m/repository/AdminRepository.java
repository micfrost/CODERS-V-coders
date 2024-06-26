package com.dci.a3m.repository;

import com.dci.a3m.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin, Long> {
    Admin findByUserEmail(String email);
}
