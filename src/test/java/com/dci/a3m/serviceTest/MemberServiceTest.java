package com.dci.a3m.serviceTest;

import com.dci.a3m.entity.Member;
import com.dci.a3m.entity.Token;
import com.dci.a3m.entity.User;
import com.dci.a3m.repository.MemberRepository;
import com.dci.a3m.repository.TokenRepository;
import com.dci.a3m.service.MemberServiceImpl;
import com.dci.a3m.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private TokenRepository tokenRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private MemberServiceImpl memberService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findAll() {
        Member member1 = new Member();
        member1.setId(1L);
        Member member2 = new Member();
        member2.setId(2L);
        when(memberRepository.findAll()).thenReturn(Arrays.asList(member1, member2));

        List<Member> members = memberService.findAll();
        assertEquals(2, members.size());
    }

    @Test
    void findById() {
        Member member = new Member();
        member.setId(1L);
        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));

        Member foundMember = memberService.findById(1L);
        assertNotNull(foundMember);
        assertEquals(1L, foundMember.getId());
    }

    @Test
    void findByUsername() {
        User user = new User();
        user.setUsername("WillymWoods");
        Member member = new Member();
        member.setUser(user);
        user.setMember(member);
        when(userService.findByUsername("WillymWoods")).thenReturn(user);

        Member foundMember = memberService.findByUsername("WillymWoods");
        assertNotNull(foundMember);
        assertEquals("WillymWoods", foundMember.getUser().getUsername());
    }

    @Test
    void save() {
        Member member = new Member();
        memberService.save(member);
        verify(memberRepository, times(1)).save(member);
    }

    @Test
    void update() {
        Member member = new Member();
        member.setId(1L); // Ensure ID is set
        memberService.update(member);
        verify(memberRepository, times(1)).save(member);
    }

    @Test
    void deleteById() {
        memberService.deleteById(1L);
        verify(memberRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteAll() {
        memberService.deleteAll();
        verify(memberRepository, times(1)).deleteAll();
    }

    @Test
    void findByUser_Username() {
        Member member = new Member();
        User user = new User();
        user.setUsername("WillymWoods");
        member.setUser(user);
        when(memberRepository.findByUser_Username("WillymWoods")).thenReturn(Optional.of(member));

        Member foundMember = memberService.findByUser_Username("WillymWoods");
        assertNotNull(foundMember);
        assertEquals("WillymWoods", foundMember.getUser().getUsername());
    }

    @Test
    void findByEmail() {
        Member member = new Member();
        User user = new User();
        user.setEmail("WillymWoods@example.com");
        member.setUser(user);
        when(memberRepository.findByUser_Email("WillymWoods@example.com")).thenReturn(member);

        Member foundMember = memberService.findByEmail("WillymWoods@example.com");
        assertNotNull(foundMember);
        assertEquals("WillymWoods@example.com", foundMember.getUser().getEmail());
    }

    @Test
    void findByToken() {
        Token token = new Token();
        token.setToken("sampletoken");
        User user = new User();
        token.setUser(user);
        Member member = new Member();
        member.setUser(user);

        when(tokenRepository.findByToken("sampletoken")).thenReturn(token);
        when(memberRepository.findByUser(user)).thenReturn(member);

        Member foundMember = memberService.findByToken("sampletoken");
        assertNotNull(foundMember);
        assertEquals(user, foundMember.getUser());
    }

}
