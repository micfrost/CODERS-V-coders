package com.dci.a3m.service;

import com.dci.a3m.databaseLoader.DatabaseLoader;
import com.dci.a3m.entity.Member;
import com.dci.a3m.entity.Token;
import com.dci.a3m.entity.User;
import com.dci.a3m.exception.UserNotFoundException;
import com.dci.a3m.repository.MemberRepository;
import com.dci.a3m.repository.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final UserService userService;
    private final DatabaseLoader databaseLoader;
    private final TokenRepository tokenRepository;

    @Autowired
    public MemberServiceImpl(MemberRepository memberRepository, UserService userService, DatabaseLoader databaseLoader, TokenRepository tokenRepository) {
        this.memberRepository = memberRepository;
        this.userService = userService;
        this.databaseLoader = databaseLoader;
        this.tokenRepository = tokenRepository;
    }


    // CRUD OPERATIONS

    // READ ALL
    @Override
    public List<Member> findAll() {
        return memberRepository.findAll();
    }

    // READ BY ID
    @Override
    public Member findById(Long id) {
        Optional<Member> result = memberRepository.findById(id);

        Member member = null;

        if (result.isPresent()) {
            member = result.get();
        } else {
            throw new UserNotFoundException("Member with id " + id + " not found.");
        }

        return member;
    }

//    // READ BY USERNAME
    @Override
    public Member findByUsername(String username) {
        // username is stored in the User entity
        User user = userService.findByUsername(username);
        if(user == null) {
           return null;
        }
        return user.getMember();
    }


    // SAVE
    @Override
    public void save(Member member) {
        memberRepository.save(member);
    }


    // UPDATE
    @Override
    public void update(Member member) {
        if (member.getId() != null) {
            memberRepository.save(member);
        } else {
            throw new IllegalArgumentException("Member ID must not be null for update operation");
        }
    }

    // DELETE
    @Override
    public void deleteById(Long id) {
        memberRepository.deleteById(id);
    }

    @Override
    public Member getAuthenticatedMember() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findByUsername(userDetails.getUsername());

        if (!user.isEnabled()) {
            throw new UserNotFoundException("User with username " + user.getUsername() + " is not enabled.");
        }

        return user.getMember();
    }

    @Override
    public void deleteAll() {
        memberRepository.deleteAll();
    }

    @Override
    public void createInitMembers() {
        databaseLoader.initMembers();
        databaseLoader.initFriendships();
    }

    @Override
    public Member findByUser_Username(String username) {
        Optional<Member> result = memberRepository.findByUser_Username(username);
        if (result.isPresent()) {
            return result.get();
        } else {
            throw new UserNotFoundException("User with username " + username + " not found.");
        }    }

    @Override
    public Member findByEmail(String email) {
        return memberRepository.findByUser_Email(email);
    }

    @Override
    public Member findByToken(String token) {
        Token tokenEntity = tokenRepository.findByToken(token);
        if (tokenEntity != null) {
            return memberRepository.findByUser(tokenEntity.getUser());
        }
        return null;
    }


}





