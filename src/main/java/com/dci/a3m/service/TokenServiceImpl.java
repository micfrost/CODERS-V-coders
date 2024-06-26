package com.dci.a3m.service;

import com.dci.a3m.entity.Member;
import com.dci.a3m.entity.Token;
import com.dci.a3m.entity.User;
import com.dci.a3m.repository.TokenRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class TokenServiceImpl implements TokenService{

    private final TokenRepository tokenRepository;
    private final UserService userService;

    public TokenServiceImpl(TokenRepository tokenRepository, UserService userService) {
        this.tokenRepository = tokenRepository;
        this.userService = userService;
    }

    @Override
    @Transactional
    public void save(Token token) {
        tokenRepository.save(token);
        User user = token.getUser();
        if(user != null) {
            user.setToken(token);
            userService.update(user);
        }
    }

    @Override
    @Transactional
    public void delete(Token token) {
        tokenRepository.delete(token);
        User user = token.getUser();
        if(user != null) {
            user.setToken(null);
            userService.update(user);
        }
    }

    @Override
    @Transactional
    public void update(Token token) {
        tokenRepository.save(token);
        User user = token.getUser();
        if(user != null) {
            user.setToken(token);
            userService.update(user);
        }
    }

    public Token findByToken(String token) {
        return tokenRepository.findByToken(token);
    }

    public Token findByUser(User user) {
        return tokenRepository.findByUser(user);
    }
}
