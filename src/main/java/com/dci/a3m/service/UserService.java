package com.dci.a3m.service;

import com.dci.a3m.entity.Member;
import com.dci.a3m.entity.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Optional;

public interface UserService {

    // User CRUD
    // READ ALL
    List<User> findAll();

    // READ BY ID
    User findById(Long id);

    // READ BY USERNAME
    User findByUsername(String username);

    // CREATE
    void save(User user);

    // UPDATE
    void update(User user);

    // DELETE
    void deleteById(Long id);


    UserDetails loadUserByUsername(String username);

    Object findByEmail(String email);

}
