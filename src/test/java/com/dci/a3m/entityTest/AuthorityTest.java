package com.dci.a3m.entityTest;

import com.dci.a3m.entity.Authority;
import com.dci.a3m.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.jupiter.api.Assertions.assertEquals;


class AuthorityTest {

    @Mock
    private User user;

    @InjectMocks
    private Authority authority;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        authority = new Authority();
        authority.setId(1L);
        authority.setUsername("AliceRiver");
        authority.setAuthority("ROLE_USER");
        authority.setUser(user);
    }

    @Test
    void testAuthorityFields() {
        assertEquals(1L, authority.getId());
        assertEquals("AliceRiver", authority.getUsername());
        assertEquals("ROLE_USER", authority.getAuthority());
        assertEquals(user, authority.getUser());
    }
}
