package com.dci.a3m.serviceTest;

import com.dci.a3m.entity.Authority;
import com.dci.a3m.repository.AuthorityRepository;
import com.dci.a3m.service.AuthorityServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class AuthorityServiceTest {

    @Mock
    private AuthorityRepository authorityRepository;

    @InjectMocks
    private AuthorityServiceImpl authorityService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAll() {
        Authority authority1 = new Authority();
        Authority authority2 = new Authority();
        List<Authority> authorities = Arrays.asList(authority1, authority2);

        when(authorityRepository.findAll()).thenReturn(authorities);

        List<Authority> result = authorityService.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void testFindById() {
        Authority authority = new Authority();
        when(authorityRepository.findById(anyLong())).thenReturn(Optional.of(authority));

        Authority result = authorityService.findById(1L);

        assertNotNull(result);
        assertEquals(authority, result);
    }

    @Test
    void testSave() {
        Authority authority = new Authority();
        authorityService.save(authority);

        verify(authorityRepository, times(1)).save(authority);
    }

    @Test
    void testUpdate() {
        Authority authority = new Authority();
        authorityService.save(authority);

        verify(authorityRepository, times(1)).save(authority);
    }

    @Test
    void testDeleteById() {
        doNothing().when(authorityRepository).deleteById(anyLong());

        authorityService.deleteById(1L);

        verify(authorityRepository, times(1)).deleteById(1L);
    }
}
