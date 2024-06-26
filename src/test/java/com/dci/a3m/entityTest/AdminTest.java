package com.dci.a3m.entityTest;

import com.dci.a3m.entity.Admin;
import com.dci.a3m.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.jupiter.api.Assertions.assertEquals;


class AdminTest {

    @Mock
    private User user;

    @InjectMocks
    private Admin admin;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        admin = new Admin();
        admin.setId(1L);
        admin.setRole("ROLE_ADMIN");
        admin.setUser(user);
    }

    @Test
    void testAdminFields() {
        assertEquals(1L, admin.getId());
        assertEquals("ROLE_ADMIN", admin.getRole());
        assertEquals(user, admin.getUser());
    }
}

