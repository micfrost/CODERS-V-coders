package com.dci.a3m.service;

import com.dci.a3m.entity.Authority;
import com.dci.a3m.entity.Member;
import com.dci.a3m.entity.User;
import com.dci.a3m.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository theUserRepository) {
        this.userRepository = theUserRepository;
    }

    // CRUD OPERATIONS

    // READ ALL
    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    // READ BY ID
    @Override
    public User findById(Long id) {
        Optional<User> result = userRepository.findById(id);

        User user = null;

        if(result.isPresent()){
            user = result.get();
        }else{
            throw new RuntimeException("User with id " + id + " not found.");
        }
        return user;
    }

    @Override
    public User findByUsername(String username) {
        Optional<User> result = userRepository.findByUsername(username);
        User user = null;
        if (result.isPresent()) {
            user = result.get();
        }
//        else {
////            throw new RuntimeException("User with username " + username + " not found.");
//        }
        return user;
    }




    // SAVE
    @Override
    public void save(User user) {
        userRepository.save(user);
    }

    // CREATE

    public User createUser(
            String username,
            String password,
            String email,
            String authorityRole) {

        Authority authority = new Authority(username, authorityRole);

        User user = new User(username, password, email, true, authority);
        return userRepository.save(user);
    }


    // UPDATE
    @Override
    public void update(User user) {
         userRepository.save(user);
    }

    // DELETE
    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isEmpty()) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        User user = userOptional.get();
        String authorityRole = user.getAuthority().getAuthority();

        // Create a collection of granted authorities
        Collection<SimpleGrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority(authorityRole));

        // Create and return UserDetails
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                authorities
        );
    }

    @Override
    public Object findByEmail(String email) {
        return userRepository.findByEmail(email);
    }


}
