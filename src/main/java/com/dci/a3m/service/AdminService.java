package com.dci.a3m.service;

import com.dci.a3m.entity.Admin;

import java.time.LocalDateTime;
import java.util.List;

public interface AdminService {

    // CRUD OPERATIONS
    // READ ALL
    List<Admin> findAll();

    // READ BY ID
    Admin findById(Long id);


    // SAVE
    void save(Admin admin);

    // CREATE
    void createAdmin(
            String username,
            String email,
            String password);

    // UPDATE
    void update(Admin admin);

    // DELETE
    void deleteById(Long id);


    Admin getAuthenticatedAdmin();

    Admin findByEmail(String email);
}
