package com.dci.a3m.repositoryTest;

import com.dci.a3m.entity.Member;
import com.dci.a3m.entity.Post;
import com.dci.a3m.repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class PostRepositoryTest {

    @Mock
    private PostRepository postRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAllByMember() {
        Member member = new Member();
        member.setId(1L);

        List<Post> posts = new ArrayList<>();
        Post post1 = new Post();
        post1.setId(1L);
        post1.setContent("Thanks for reading my Post. Kudos.");
        post1.setMediaUrl("https://images.unsplash.com/photo-1499063078284-f78f7d89616a?q=80&w=928&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D");
        posts.add(post1);

        when(postRepository.findAllByMember(member)).thenReturn(posts);

        List<Post> foundPosts = postRepository.findAllByMember(member);

        assertEquals(1, foundPosts.size());
        assertEquals(post1.getId(), foundPosts.get(0).getId());
        assertEquals(post1.getContent(), foundPosts.get(0).getContent());
        assertEquals(post1.getMediaUrl(), foundPosts.get(0).getMediaUrl());
    }

    @Test
    void testFindAll() {
        List<Post> posts = new ArrayList<>();
        Post post1 = new Post();
        post1.setId(1L);
        post1.setContent("Thanks for reading my Post. Kudos.");
        post1.setMediaUrl("https://images.unsplash.com/photo-1499063078284-f78f7d89616a?q=80&w=928&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D");
        posts.add(post1);

        when(postRepository.findAll()).thenReturn(posts);

        List<Post> foundPosts = postRepository.findAll();

        assertEquals(1, foundPosts.size());
        assertEquals(post1.getId(), foundPosts.get(0).getId());
        assertEquals(post1.getContent(), foundPosts.get(0).getContent());
        assertEquals(post1.getMediaUrl(), foundPosts.get(0).getMediaUrl());
    }

    @Test
    void testFindByMemberIdIn() {
        List<Long> memberIds = List.of(1L, 2L);

        List<Post> posts = new ArrayList<>();
        Post post1 = new Post();
        post1.setId(1L);
        post1.setContent("Thanks for reading my Post. Kudos.");
        post1.setMediaUrl("https://images.unsplash.com/photo-1499063078284-f78f7d89616a?q=80&w=928&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D");
        posts.add(post1);

        when(postRepository.findByMemberIdIn(memberIds)).thenReturn(posts);

        List<Post> foundPosts = postRepository.findByMemberIdIn(memberIds);

        assertEquals(1, foundPosts.size());
        assertEquals(post1.getId(), foundPosts.get(0).getId());
        assertEquals(post1.getContent(), foundPosts.get(0).getContent());
        assertEquals(post1.getMediaUrl(), foundPosts.get(0).getMediaUrl());
    }
}
