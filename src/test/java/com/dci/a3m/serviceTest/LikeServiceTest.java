package com.dci.a3m.serviceTest;

import com.dci.a3m.entity.Comment;
import com.dci.a3m.entity.Like;
import com.dci.a3m.entity.Member;
import com.dci.a3m.entity.Post;
import com.dci.a3m.repository.LikeRepository;
import com.dci.a3m.service.CommentService;
import com.dci.a3m.service.LikeServiceImpl;
import com.dci.a3m.service.MemberService;
import com.dci.a3m.service.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class LikeServiceTest {

    @Mock
    private LikeRepository likeRepository;

    @Mock
    private PostService postService;

    @Mock
    private MemberService memberService;

    @Mock
    private CommentService commentService;

    @InjectMocks
    private LikeServiceImpl likeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAll() {
        Like like = new Like();
        List<Like> likes = Collections.singletonList(like);

        when(likeRepository.findAll()).thenReturn(likes);

        List<Like> result = likeService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testFindById() {
        Like like = new Like();
        when(likeRepository.findById(anyLong())).thenReturn(Optional.of(like));

        Like result = likeService.findById(1L);

        assertNotNull(result);
        assertEquals(like, result);
    }

    @Test
    void testFindByMemberAndPost() {
        Member member = new Member();
        Post post = new Post();
        Like like = new Like();

        when(likeRepository.findByMemberAndPost(member, post)).thenReturn(like);

        Like result = likeService.findByMemberAndPost(member, post);

        assertNotNull(result);
        assertEquals(like, result);
    }

    @Test
    void testFindByMemberAndComment() {
        Member member = new Member();
        Comment comment = new Comment();
        Like like = new Like();

        when(likeRepository.findByMemberAndComment(member, comment)).thenReturn(like);

        Like result = likeService.findByMemberAndComment(member, comment);

        assertNotNull(result);
        assertEquals(like, result);
    }

    @Test
    void testSave() {
        Like like = new Like();
        likeService.save(like);

        verify(likeRepository, times(1)).save(like);
    }

    @Test
    void testUpdate() {
        Like like = new Like();
        like.setId(1L);
        likeService.update(like);

        verify(likeRepository, times(1)).save(like);
    }

    @Test
    void testDeleteById() {
        doNothing().when(likeRepository).deleteById(anyLong());

        likeService.deleteById(1L);

        verify(likeRepository, times(1)).deleteById(1L);
    }

    @Test
    void testHasMemberLikedPost() {
        Member member = new Member();
        Post post = new Post();

        when(likeRepository.existsByMemberAndPost(member, post)).thenReturn(true);

        boolean result = likeService.hasMemberLikedPost(member, post);

        assertTrue(result);
    }

    @Test
    void testHasMemberLikedComment() {
        Member member = new Member();
        Comment comment = new Comment();

        when(likeRepository.existsByMemberAndComment(member, comment)).thenReturn(true);

        boolean result = likeService.hasMemberLikedComment(member, comment);

        assertTrue(result);
    }
}
