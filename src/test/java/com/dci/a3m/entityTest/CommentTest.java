package com.dci.a3m.entityTest;

import com.dci.a3m.entity.Comment;
import com.dci.a3m.entity.Member;
import com.dci.a3m.entity.Post;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.assertEquals;


class CommentTest {

    @Mock
    private Member member;

    @Mock
    private Post post;

    @InjectMocks
    private Comment comment;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        comment = new Comment();
        comment.setId(1L);
        comment.setContent("Hava a lovely day. See yeah.");
        comment.setMember(member);
        comment.setPost(post);
        comment.setCreatedAt(LocalDateTime.now());
    }

    @Test
    void testCommentFields() {
        assertEquals(1L, comment.getId());
        assertEquals("Hava a lovely day. See yeah.", comment.getContent());
        assertEquals(member, comment.getMember());
        assertEquals(post, comment.getPost());
    }
}
