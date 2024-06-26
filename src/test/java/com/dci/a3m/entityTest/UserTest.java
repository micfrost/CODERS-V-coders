package com.dci.a3m.entityTest;

import com.dci.a3m.entity.User;
import com.dci.a3m.entity.Authority;
import com.dci.a3m.entity.Admin;
import com.dci.a3m.entity.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserTest {

    @Mock
    private Authority authority;

    @Mock
    private Admin admin;

    @Mock
    private Member member;

    @InjectMocks
    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setId(1L);
        user.setUsername("EmmaOcean");
        user.setEmail("EmmaOcean@example.com");
        user.setPassword("EmmaOcean");
        user.setEnabled(true);
        user.setAuthority(authority);
        user.setAdmin(admin);
        user.setMember(member);
    }

    @Test
    void testUserFields() {
        assertEquals(1L, user.getId());
        assertEquals("EmmaOcean", user.getUsername());
        assertEquals("EmmaOcean@example.com", user.getEmail());
        assertEquals("EmmaOcean", user.getPassword());
        assertEquals(true, user.isEnabled());
        assertEquals(authority, user.getAuthority());
        assertEquals(admin, user.getAdmin());
        assertEquals(member, user.getMember());
    }
}