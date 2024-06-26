package com.dci.a3m.ControllerMVCTest;

import com.dci.a3m.controller.LikeControllerMVC;
import com.dci.a3m.entity.Comment;
import com.dci.a3m.entity.Like;
import com.dci.a3m.entity.Member;
import com.dci.a3m.entity.Post;
import com.dci.a3m.service.CommentService;
import com.dci.a3m.service.EmailService;
import com.dci.a3m.service.LikeService;
import com.dci.a3m.service.MemberService;
import com.dci.a3m.service.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class LikeControllerMVCTest {

    @Mock
    private MemberService memberService;

    @Mock
    private PostService postService;

    @Mock
    private LikeService likeService;

    @Mock
    private CommentService commentService;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private LikeControllerMVC likeControllerMVC;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testLikePostInfo() {
        Member member = new Member();
        member.setId(1L);
        Post post = new Post();
        post.setId(1L);
        post.setLikes(new ArrayList<>());
        when(memberService.getAuthenticatedMember()).thenReturn(member);
        when(postService.findById(1L)).thenReturn(post);
        when(likeService.findByMemberAndPost(member, post)).thenReturn(null);

        String viewName = likeControllerMVC.likePostInfo(1L);
        assertEquals("redirect:/mvc/posts/?postId=1", viewName);
        verify(likeService).save(any(Like.class));
        verify(emailService).sendLikeEmail(eq(post.getMember()), eq(member));
    }

    @Test
    public void testUnlikePostInfo() {
        Member member = new Member();
        member.setId(1L);
        Post post = new Post();
        post.setId(1L);
        post.setLikes(new ArrayList<>());
        Like like = new Like(member, post);
        like.setId(1L);
        when(memberService.getAuthenticatedMember()).thenReturn(member);
        when(postService.findById(1L)).thenReturn(post);
        when(likeService.findByMemberAndPost(member, post)).thenReturn(like);

        String viewName = likeControllerMVC.unlikePostInfo(1L);
        assertEquals("redirect:/mvc/posts/?postId=1", viewName);
        verify(likeService).deleteById(like.getId());
    }

    @Test
    public void testLikePostProfile() {
        Member member = new Member();
        member.setId(1L);
        Post post = new Post();
        post.setId(1L);
        post.setLikes(new ArrayList<>());
        Member postOwner = new Member();
        postOwner.setId(2L);
        post.setMember(postOwner);
        when(memberService.getAuthenticatedMember()).thenReturn(member);
        when(postService.findById(1L)).thenReturn(post);
        when(likeService.findByMemberAndPost(member, post)).thenReturn(null);

        String viewName = likeControllerMVC.likePostProfile(1L);
        assertEquals("redirect:/mvc/members/?memberId=" + postOwner.getId(), viewName);
        verify(likeService).save(any(Like.class));
        verify(emailService).sendLikeEmail(eq(postOwner), eq(member));
    }

    @Test
    public void testUnlikePostProfile() {
        Member member = new Member();
        member.setId(1L);
        Post post = new Post();
        post.setId(1L);
        post.setLikes(new ArrayList<>());
        Like like = new Like(member, post);
        like.setId(1L);
        Member postOwner = new Member();
        postOwner.setId(2L);
        post.setMember(postOwner);
        when(memberService.getAuthenticatedMember()).thenReturn(member);
        when(postService.findById(1L)).thenReturn(post);
        when(likeService.findByMemberAndPost(member, post)).thenReturn(like);

        String viewName = likeControllerMVC.unlikePostProfile(1L);
        assertEquals("redirect:/mvc/members/?memberId=" + postOwner.getId(), viewName);
        verify(likeService).deleteById(like.getId());
    }

    @Test
    public void testLikeComment() {
        Member member = new Member();
        member.setId(1L);
        Comment comment = new Comment();
        comment.setId(1L);
        comment.setLikes(new ArrayList<>());
        Post post = new Post();
        post.setId(1L);
        comment.setPost(post);
        Member commentOwner = new Member();
        commentOwner.setId(2L);
        comment.setMember(commentOwner);
        when(memberService.getAuthenticatedMember()).thenReturn(member);
        when(commentService.findById(1L)).thenReturn(comment);
        when(likeService.findByMemberAndComment(member, comment)).thenReturn(null);

        String viewName = likeControllerMVC.likeComment(1L);
        assertEquals("redirect:/mvc/posts/?postId=" + post.getId(), viewName);
        verify(likeService).save(any(Like.class));
        verify(emailService).sendLikeCommentEmail(eq(commentOwner), eq(member));
    }

    @Test
    public void testUnlikeComment() {
        Member member = new Member();
        member.setId(1L);
        Comment comment = new Comment();
        comment.setId(1L);
        comment.setLikes(new ArrayList<>());
        Post post = new Post();
        post.setId(1L);
        comment.setPost(post);
        Like like = new Like(member, comment);
        like.setId(1L);
        when(memberService.getAuthenticatedMember()).thenReturn(member);
        when(commentService.findById(1L)).thenReturn(comment);
        when(likeService.findByMemberAndComment(member, comment)).thenReturn(like);

        String viewName = likeControllerMVC.unlikeComment(1L);
        assertEquals("redirect:/mvc/posts/?postId=" + post.getId(), viewName);
        verify(likeService).deleteById(like.getId());
    }
}
