package com.dci.a3m.entityTest;

import com.dci.a3m.entity.Member;
import com.dci.a3m.entity.Post;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.assertEquals;


class PostTest {

    @Mock
    private Member member;

    @InjectMocks
    private Post post;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        post = new Post();
        post.setId(1L);
        post.setContent("Thanks for reading my Post. Kudos.");
        post.setCreatedAt(LocalDateTime.now());
        post.setMember(member);
    }

    @Test
    void testPostFields() {
        assertEquals(1L, post.getId());
        assertEquals("Thanks for reading my Post. Kudos.", post.getContent());
        assertEquals(member, post.getMember());
    }
}
