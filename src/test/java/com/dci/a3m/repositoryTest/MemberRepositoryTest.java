package com.dci.a3m.repositoryTest;

import com.dci.a3m.entity.Member;
import com.dci.a3m.entity.User;
import com.dci.a3m.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class MemberRepositoryTest {

    @Mock
    private MemberRepository memberRepository;

    private Member testMember;
    private User testUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        testUser = new User();
        testUser.setUsername("ThomasLake");
        testUser.setEmail("ThomasLake@example.com");
        testUser.setPassword("ThomasLake");

        testMember = new Member();
        testMember.setUser(testUser);
    }

    @Test
    void testFindByUser_Username() {
        when(memberRepository.findByUser_Username(anyString())).thenReturn(Optional.of(testMember));

        Optional<Member> result = memberRepository.findByUser_Username("ThomasLake");

        assertTrue(result.isPresent());
        assertEquals(testMember, result.get());
        verify(memberRepository, times(1)).findByUser_Username("ThomasLake");
    }

    @Test
    void testFindByUser_Email() {
        when(memberRepository.findByUser_Email(anyString())).thenReturn(testMember);

        Member result = memberRepository.findByUser_Email("ThomasLake@example.com");

        assertNotNull(result);
        assertEquals(testMember, result);
        verify(memberRepository, times(1)).findByUser_Email("ThomasLake@example.com");
    }

    @Test
    void testFindByUser() {
        when(memberRepository.findByUser(any(User.class))).thenReturn(testMember);

        Member result = memberRepository.findByUser(testUser);

        assertNotNull(result);
        assertEquals(testMember, result);
        verify(memberRepository, times(1)).findByUser(testUser);
    }
}
