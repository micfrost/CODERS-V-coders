package com.dci.a3m.service;

import com.dci.a3m.entity.Token;
import com.dci.a3m.entity.User;

public interface TokenService {
    void save(Token token);

    void delete(Token token);

    void update(Token token);

    Token findByToken(String token);

    Token findByUser(User user);
}
