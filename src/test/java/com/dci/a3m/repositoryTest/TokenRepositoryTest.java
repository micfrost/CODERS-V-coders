package com.dci.a3m.repositoryTest;

import com.dci.a3m.entity.Token;
import com.dci.a3m.entity.User;
import com.dci.a3m.repository.TokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class TokenRepositoryTest {

    @Mock
    private TokenRepository tokenRepository;

    private Token testToken;
    private User testUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        testUser = new User();
        testUser.setUsername("AliceRiver");
        testUser.setEmail("AliceRiver@example.com");
        testUser.setPassword("AliceRiver");

        testToken = new Token();
        testToken.setToken("testToken");
        testToken.setUser(testUser);
    }

    @Test
    void testFindByToken() {
        when(tokenRepository.findByToken(anyString())).thenReturn(testToken);

        Token result = tokenRepository.findByToken("testToken");

        assertNotNull(result);
        assertEquals(testToken, result);
        verify(tokenRepository, times(1)).findByToken("testToken");
    }

    @Test
    void testFindByUser() {
        when(tokenRepository.findByUser(any(User.class))).thenReturn(testToken);

        Token result = tokenRepository.findByUser(testUser);

        assertNotNull(result);
        assertEquals(testToken, result);
        verify(tokenRepository, times(1)).findByUser(testUser);
    }
}
