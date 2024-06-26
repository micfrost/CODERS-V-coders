package com.dci.a3m.entityTest;

import com.dci.a3m.entity.Token;
import com.dci.a3m.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class TokenTest {

    @Mock
    private User user;

    @InjectMocks
    private Token token;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        token = new Token();
        token.setId(1L);
        token.setToken("sample-token");
        token.setCreationDate(LocalDateTime.now());
        token.setExpirationDate(LocalDateTime.now().plusDays(1));
        token.setUser(user);
    }

    @Test
    void testTokenFields() {
        assertEquals(1L, token.getId());
        assertEquals("sample-token", token.getToken());
        assertEquals(user, token.getUser());
        assertEquals(LocalDateTime.now().plusDays(1).getDayOfYear(), token.getExpirationDate().getDayOfYear());
    }

}
