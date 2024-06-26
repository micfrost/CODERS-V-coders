package com.dci.a3m.repositoryTest;

import com.dci.a3m.entity.Admin;
import com.dci.a3m.repository.AdminRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AdminRepositoryTest {

    @Mock
    private AdminRepository adminRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindByUserEmail() {
        String email = "admin@example.com";
        Admin admin = new Admin();
        admin.setId(1L);
        admin.setRole("ROLE_ADMIN");

        when(adminRepository.findByUserEmail(email)).thenReturn(admin);

        Admin foundAdmin = adminRepository.findByUserEmail(email);

        assertNotNull(foundAdmin);
        assertEquals(admin.getId(), foundAdmin.getId());
        assertEquals(admin.getRole(), foundAdmin.getRole());
        verify(adminRepository, times(1)).findByUserEmail(email);
    }
}
