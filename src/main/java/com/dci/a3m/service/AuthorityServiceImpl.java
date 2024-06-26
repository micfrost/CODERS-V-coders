package com.dci.a3m.service;


import com.dci.a3m.entity.Authority;
import com.dci.a3m.repository.AuthorityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AuthorityServiceImpl implements AuthorityService {

    private AuthorityRepository authorityRepository;

    @Autowired
    public AuthorityServiceImpl(AuthorityRepository theAuthorityRepository) {
        this.authorityRepository = theAuthorityRepository;
    }

    // CRUD OPERATIONS

    // READ ALL
    @Override
    public List<Authority> findAll() {
        return authorityRepository.findAll();
    }

    // READ BY ID
    @Override
    public Authority findById(Long id) {
        Optional<Authority> result = authorityRepository.findById(id);

        Authority authority = null;

        if(result.isPresent()){
            authority = result.get();
        }else{
            throw new RuntimeException("Authority with id " + id + " not found.");
        }
        return authority;
    }

    // CREATE
    @Override
    public void save(Authority authority) {
         authorityRepository.save(authority);
    }


    // UPDATE
    @Override
    public void update(Authority authority) {
         authorityRepository.save(authority);
    }

    // DELETE
    @Override
    public void deleteById(Long id) {
        authorityRepository.deleteById(id);
    }
}