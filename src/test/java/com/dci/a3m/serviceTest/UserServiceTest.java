package com.dci.a3m.serviceTest;

import com.dci.a3m.entity.Authority;
import com.dci.a3m.entity.User;
import com.dci.a3m.repository.UserRepository;
import com.dci.a3m.service.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAll() {
        User user = new User();
        List<User> users = Collections.singletonList(user);

        when(userRepository.findAll()).thenReturn(users);

        List<User> result = userService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testFindById() {
        User user = new User();
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        User result = userService.findById(1L);

        assertNotNull(result);
        assertEquals(user, result);
    }

    @Test
    void testFindByUsername() {
        User user = new User();
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));

        User result = userService.findByUsername("AliceRiver");

        assertNotNull(result);
        assertEquals(user, result);
    }

    @Test
    void testSave() {
        User user = new User();
        userService.save(user);

        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testUpdate() {
        User user = new User();
        userService.update(user);

        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testDeleteById() {
        doNothing().when(userRepository).deleteById(anyLong());

        userService.deleteById(1L);

        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void testLoadUserByUsername() {
        User user = new User();
        user.setUsername("AliceRiver");
        user.setPassword("AliceRiver");
        user.setEnabled(true);

        Authority authority = new Authority();
        authority.setAuthority("ROLE_USER");
        user.setAuthority(authority);

        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));

        UserDetails userDetails = userService.loadUserByUsername("AliceRiver");

        assertNotNull(userDetails);
        assertEquals(user.getUsername(), userDetails.getUsername());
        assertEquals("AliceRiver", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_USER")));
    }

    @Test
    void testLoadUserByUsernameNotFound() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> {
            userService.loadUserByUsername("AliceRiver");
        });
    }

    @Test
    void testFindByEmail() {
        User user = new User();
        when(userRepository.findByEmail(anyString())).thenReturn(user);

        Object result = userService.findByEmail("AliceRiver@example.com");

        assertNotNull(result);
        assertEquals(user, result);
    }
}
