package com.dci.a3m.serviceTest;

import com.dci.a3m.entity.Member;
import com.dci.a3m.entity.Post;
import com.dci.a3m.repository.CommentRepository;
import com.dci.a3m.repository.MemberRepository;
import com.dci.a3m.repository.PostRepository;
import com.dci.a3m.service.PostServiceImpl;
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

class PostServiceTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private PostServiceImpl postService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAll() {
        Post post = new Post();
        List<Post> posts = Collections.singletonList(post);

        when(postRepository.findAll()).thenReturn(posts);

        List<Post> result = postService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testFindAllByMember() {
        Member member = new Member();
        Post post = new Post();
        List<Post> posts = Collections.singletonList(post);

        when(postRepository.findAllByMember(member)).thenReturn(posts);

        List<Post> result = postService.findAllByMember(member);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testFindById() {
        Post post = new Post();
        when(postRepository.findById(anyLong())).thenReturn(Optional.of(post));

        Post result = postService.findById(1L);

        assertNotNull(result);
        assertEquals(post, result);
    }

    @Test
    void testSave() {
        Post post = new Post();
        postService.save(post);

        verify(postRepository, times(1)).save(post);
    }

    @Test
    void testUpdate() {
        Post post = new Post();
        post.setId(1L);
        postService.update(post);

        verify(postRepository, times(1)).save(post);
    }

    @Test
    void testDeleteById() {
        doNothing().when(postRepository).deleteById(anyLong());

        postService.deleteById(1L);

        verify(postRepository, times(1)).deleteById(1L);
    }

    @Test
    void testGetRandomMediaUrl() {
        String url = postService.getRandomMediaUrl();
        assertNotNull(url);
        assertTrue(url.startsWith("https://images.unsplash.com/photo"));
    }
}
