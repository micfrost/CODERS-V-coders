package com.dci.a3m.service;

import com.dci.a3m.entity.Member;

import java.util.List;
import java.util.Optional;

public interface MemberService {

    // CRUD OPERATIONS
    // READ ALL
    List<Member> findAll();

    // READ BY ID
    Member findById(Long id);

    // READ BY USERNAME
    Member findByUsername(String username);


    // SAVE
    void save(Member member);

    // UPDATE
    void update(Member member);

    // DELETE BY ID
    void deleteById(Long id);

    // AUTHENTICATION
    public Member getAuthenticatedMember();

    // DELETE ALL
    void deleteAll();

    void createInitMembers();

    Member findByUser_Username(String username);

    Member findByEmail(String email);


    Member findByToken(String token);
}

