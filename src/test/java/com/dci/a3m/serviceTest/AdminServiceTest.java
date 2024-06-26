package com.dci.a3m.serviceTest;

import com.dci.a3m.entity.Admin;
import com.dci.a3m.entity.User;
import com.dci.a3m.repository.AdminRepository;
import com.dci.a3m.repository.UserRepository;
import com.dci.a3m.service.AdminServiceImpl;
import com.dci.a3m.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class AdminServiceTest {

    @Mock
    private AdminRepository adminRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserService userService;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @InjectMocks
    private AdminServiceImpl adminService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void testFindAll() {
        Admin admin1 = new Admin();
        Admin admin2 = new Admin();
        List<Admin> admins = Arrays.asList(admin1, admin2);

        when(adminRepository.findAll()).thenReturn(admins);

        List<Admin> result = adminService.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void testFindById() {
        Admin admin = new Admin();
        when(adminRepository.findById(anyLong())).thenReturn(Optional.of(admin));

        Admin result = adminService.findById(1L);

        assertNotNull(result);
        assertEquals(admin, result);
    }

    @Test
    void testSave() {
        Admin admin = new Admin();
        adminService.save(admin);

        verify(adminRepository, times(1)).save(admin);
    }

    @Test
    void testCreateAdmin() {
        doAnswer(invocation -> null).when(userRepository).save(any());

        adminService.createAdmin("admin2", "admin2@example.com", "admin2");

        verify(userRepository, times(1)).save(any());
    }

    @Test
    void testUpdate() {
        Admin admin = new Admin();
        adminService.save(admin);

        verify(adminRepository, times(1)).save(admin);
    }

    @Test
    void testDeleteById() {
        doNothing().when(adminRepository).deleteById(anyLong());

        adminService.deleteById(1L);

        verify(adminRepository, times(1)).deleteById(1L);
    }

    @Test
    void testGetAuthenticatedAdmin() {
        UserDetails userDetails = mock(UserDetails.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn("admin@example.com");

        User user = new User();
        Admin admin = new Admin();
        user.setAdmin(admin);

        when(userService.findByUsername(anyString())).thenReturn(user);

        Admin result = adminService.getAuthenticatedAdmin();

        assertNotNull(result);
        assertEquals(admin, result);
    }

    @Test
    void testFindByEmail() {
        Admin admin = new Admin();
        when(adminRepository.findByUserEmail(anyString())).thenReturn(admin);

        Admin result = adminService.findByEmail("admin@example.com");

        assertNotNull(result);
        assertEquals(admin, result);
    }
}
