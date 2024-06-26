package com.dci.a3m.repositoryTest;

import com.dci.a3m.entity.Comment;
import com.dci.a3m.entity.Like;
import com.dci.a3m.entity.Member;
import com.dci.a3m.entity.Post;
import com.dci.a3m.repository.LikeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LikeRepositoryTest {

    @Mock
    private LikeRepository likeRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindByMemberAndPost() {
        Member member = new Member();
        Post post = new Post();
        Like like = new Like(member, post);

        when(likeRepository.findByMemberAndPost(member, post)).thenReturn(like);

        Like result = likeRepository.findByMemberAndPost(member, post);

        assertNotNull(result);
        assertEquals(member, result.getMember());
        assertEquals(post, result.getPost());
        verify(likeRepository, times(1)).findByMemberAndPost(member, post);
    }

    @Test
    void testFindByMemberAndComment() {
        Member member = new Member();
        Comment comment = new Comment();
        Like like = new Like(member, comment);

        when(likeRepository.findByMemberAndComment(member, comment)).thenReturn(like);

        Like result = likeRepository.findByMemberAndComment(member, comment);

        assertNotNull(result);
        assertEquals(member, result.getMember());
        assertEquals(comment, result.getComment());
        verify(likeRepository, times(1)).findByMemberAndComment(member, comment);
    }

    @Test
    void testExistsByMemberAndPost() {
        Member member = new Member();
        Post post = new Post();

        when(likeRepository.existsByMemberAndPost(member, post)).thenReturn(true);

        boolean exists = likeRepository.existsByMemberAndPost(member, post);

        assertTrue(exists);
        verify(likeRepository, times(1)).existsByMemberAndPost(member, post);
    }

    @Test
    void testExistsByMemberAndComment() {
        Member member = new Member();
        Comment comment = new Comment();

        when(likeRepository.existsByMemberAndComment(member, comment)).thenReturn(true);

        boolean exists = likeRepository.existsByMemberAndComment(member, comment);

        assertTrue(exists);
        verify(likeRepository, times(1)).existsByMemberAndComment(member, comment);
    }
}
