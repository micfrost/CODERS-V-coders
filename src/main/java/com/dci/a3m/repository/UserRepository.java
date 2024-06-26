package com.dci.a3m.repository;

import com.dci.a3m.entity.Member;
import com.dci.a3m.entity.Token;
import com.dci.a3m.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long> {
   Optional<User> findByUsername(String username);

    Object findByEmail(String email);


}
