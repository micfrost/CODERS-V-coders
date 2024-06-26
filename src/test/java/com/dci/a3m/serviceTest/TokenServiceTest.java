package com.dci.a3m.serviceTest;

import com.dci.a3m.entity.Token;
import com.dci.a3m.entity.User;
import com.dci.a3m.repository.TokenRepository;
import com.dci.a3m.service.TokenServiceImpl;
import com.dci.a3m.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TokenServiceTest {

    @Mock
    private TokenRepository tokenRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private TokenServiceImpl tokenService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void save() {
        Token token = new Token();
        User user = new User();
        token.setUser(user);
        tokenService.save(token);

        verify(tokenRepository, times(1)).save(token);
        verify(userService, times(1)).update(user);
        assertEquals(token, user.getToken());
    }

    @Test
    void delete() {
        Token token = new Token();
        User user = new User();
        token.setUser(user);
        user.setToken(token);
        tokenService.delete(token);

        verify(tokenRepository, times(1)).delete(token);
        verify(userService, times(1)).update(user);
        assertNull(user.getToken());
    }

    @Test
    void update() {
        Token token = new Token();
        User user = new User();
        token.setUser(user);
        tokenService.update(token);

        verify(tokenRepository, times(1)).save(token);
        verify(userService, times(1)).update(user);
        assertEquals(token, user.getToken());
    }

    @Test
    void findByToken() {
        Token token = new Token();
        token.setToken("sampletoken");
        when(tokenRepository.findByToken("sampletoken")).thenReturn(token);

        Token foundToken = tokenService.findByToken("sampletoken");
        assertNotNull(foundToken);
        assertEquals("sampletoken", foundToken.getToken());
    }

    @Test
    void findByUser() {
        User user = new User();
        Token token = new Token();
        token.setUser(user);
        when(tokenRepository.findByUser(user)).thenReturn(token);

        Token foundToken = tokenService.findByUser(user);
        assertNotNull(foundToken);
        assertEquals(user, foundToken.getUser());
    }
}
