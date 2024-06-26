package com.dci.a3m.entityTest;

import com.dci.a3m.entity.Comment;
import com.dci.a3m.entity.Like;
import com.dci.a3m.entity.Member;
import com.dci.a3m.entity.Post;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.jupiter.api.Assertions.assertEquals;


class LikeTest {

    @Mock
    private Member member;

    @Mock
    private Post post;

    @Mock
    private Comment comment;

    @InjectMocks
    private Like like;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        like = new Like();
        like.setId(1L);
        like.setMember(member);
        like.setPost(post);
        like.setComment(comment);
    }

    @Test
    void testLikeFields() {
        assertEquals(1L, like.getId());
        assertEquals(member, like.getMember());
        assertEquals(post, like.getPost());
        assertEquals(comment, like.getComment());
    }
}
