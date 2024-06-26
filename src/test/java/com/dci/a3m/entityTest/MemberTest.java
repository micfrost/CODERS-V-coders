package com.dci.a3m.entityTest;

import com.dci.a3m.entity.Member;
import com.dci.a3m.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.assertEquals;


class MemberTest {

    @Mock
    private User user;

    @InjectMocks
    private Member member;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        member = new Member();
        member.setId(1L);
        member.setFirstName("Thomas");
        member.setLastName("Lake");
        member.setBirthDate(LocalDate.of(1990, 1, 1));
        member.setUser(user);
    }

    @Test
    void testMemberFields() {
        assertEquals(1L, member.getId());
        assertEquals("Thomas", member.getFirstName());
        assertEquals("Lake", member.getLastName());
        assertEquals(LocalDate.of(1990, 1, 1), member.getBirthDate());
        assertEquals(user, member.getUser());
    }
}

