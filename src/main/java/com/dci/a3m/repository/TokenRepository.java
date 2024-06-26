package com.dci.a3m.repository;

import com.dci.a3m.entity.Token;
import com.dci.a3m.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<Token, Long> {
    Token findByToken(String token);

    Token findByUser(User user);
}
