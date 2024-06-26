package com.dci.a3m.repositoryTest;

import com.dci.a3m.entity.User;
import com.dci.a3m.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class UserRepositoryTest {

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindByUsername() {
        User user = new User();
        user.setId(5L);
        user.setUsername("EmmaOcean");
        user.setEmail("EmmaOcean@example.com");
        user.setPassword("$2a$10$HKUdAeslOKaxTw4jIwM1SuZYZgd8zo/ftppcyFN/DfVlmt3ohJfTm");

        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));

        Optional<User> foundUser = userRepository.findByUsername("EmmaOcean");

        assertTrue(foundUser.isPresent());
        assertEquals(user.getId(), foundUser.get().getId());
        assertEquals(user.getUsername(), foundUser.get().getUsername());
        assertEquals(user.getEmail(), foundUser.get().getEmail());
        assertEquals(user.getPassword(), foundUser.get().getPassword());
    }

    @Test
    void testFindByEmail() {
        User user = new User();
        user.setId(5L);
        user.setUsername("EmmaOcean");
        user.setEmail("EmmaOcean@example.com");
        user.setPassword("$2a$10$HKUdAeslOKaxTw4jIwM1SuZYZgd8zo/ftppcyFN/DfVlmt3ohJfTm");

        when(userRepository.findByEmail(anyString())).thenReturn(user);

        Optional<User> foundUser = Optional.ofNullable((User) userRepository.findByEmail("EmmaOcean@example.com"));

        assertTrue(foundUser.isPresent());
        assertEquals(user.getId(), foundUser.get().getId());
        assertEquals(user.getUsername(), foundUser.get().getUsername());
        assertEquals(user.getEmail(), foundUser.get().getEmail());
        assertEquals(user.getPassword(), foundUser.get().getPassword());
    }
}
