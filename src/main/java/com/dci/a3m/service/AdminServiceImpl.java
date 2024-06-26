package com.dci.a3m.service;

import com.dci.a3m.entity.Admin;
import com.dci.a3m.entity.Authority;
import com.dci.a3m.entity.User;
import com.dci.a3m.repository.AdminRepository;
import com.dci.a3m.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdminServiceImpl implements AdminService {

    private final AdminRepository adminRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;

    @Autowired
    public AdminServiceImpl(AdminRepository adminRepository, UserRepository userRepository, PasswordEncoder passwordEncoder, UserService userService) {
        this.adminRepository = adminRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
    }


    // CRUD OPERATIONS

    // READ ALL
    @Override
    public List<Admin> findAll() {
        return adminRepository.findAll();
    }

    // READ BY ID
    @Override
    public Admin findById(Long id) {
        Optional<Admin> result = adminRepository.findById(id);

        Admin admin = null;

        if (result.isPresent()) {
            admin = result.get();
        } else {
            throw new RuntimeException("Admin with id " + id + " not found.");
        }

        return admin;
    }

    // SAVE
    @Override
    public void save(Admin admin) {
        adminRepository.save(admin);
    }

    // CREATE
    @Override
    public void createAdmin(
            String username,
            String email,
            String password) {

        Admin admin = new Admin();

        Authority authority = new Authority(username, admin.getRole());

        User user = new User(username, email, password, true, authority, admin);

        userRepository.save(user);
    }

    // UPDATE
    @Override
    public void update(Admin admin) {
        if (admin.getId() != null) {
            adminRepository.save(admin);
        } else {
            throw new IllegalArgumentException("Admin ID must not be null for update operation");
        }
    }



    // DELETE BY ID
    @Override
    public void deleteById(Long id) {
        adminRepository.deleteById(id);
    }

    @Override
    public Admin getAuthenticatedAdmin() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findByUsername(userDetails.getUsername());
        return user.getAdmin();
    }

    @Override
    public Admin findByEmail(String email) {
        return adminRepository.findByUserEmail(email);
    }


}
