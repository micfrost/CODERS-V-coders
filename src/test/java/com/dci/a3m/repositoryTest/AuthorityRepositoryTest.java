package com.dci.a3m.repositoryTest;

import com.dci.a3m.entity.Authority;
import com.dci.a3m.repository.AuthorityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthorityRepositoryTest {

    @Mock
    private AuthorityRepository authorityRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindById() {
        Long authorityId = 1L;
        Authority authority = new Authority();
        authority.setId(authorityId);
        authority.setUsername("WillWood");
        authority.setAuthority("ROLE_USER");

        when(authorityRepository.findById(authorityId)).thenReturn(Optional.of(authority));

        Optional<Authority> foundAuthority = authorityRepository.findById(authorityId);

        assertTrue(foundAuthority.isPresent());
        assertEquals(authority.getId(), foundAuthority.get().getId());
        assertEquals(authority.getUsername(), foundAuthority.get().getUsername());
        assertEquals(authority.getAuthority(), foundAuthority.get().getAuthority());
        verify(authorityRepository, times(1)).findById(authorityId);
    }

    @Test
    void testSave() {
        Authority authority = new Authority();
        authority.setUsername("AdminExample");
        authority.setAuthority("ROLE_ADMIN");

        when(authorityRepository.save(authority)).thenReturn(authority);

        Authority savedAuthority = authorityRepository.save(authority);

        assertNotNull(savedAuthority);
        assertEquals(authority.getUsername(), savedAuthority.getUsername());
        assertEquals(authority.getAuthority(), savedAuthority.getAuthority());
        verify(authorityRepository, times(1)).save(authority);
    }

    @Test
    void testDelete() {
        Long authorityId = 1L;

        doNothing().when(authorityRepository).deleteById(authorityId);

        authorityRepository.deleteById(authorityId);

        verify(authorityRepository, times(1)).deleteById(authorityId);
    }
}
