package com.dci.a3m.ControllerMVCTest;

import com.dci.a3m.controller.PostControllerMVC;
import com.dci.a3m.entity.*;
import com.dci.a3m.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class PostControllerMVCTest {

    @Mock
    private PostService postService;

    @Mock
    private MemberService memberService;

    @Mock
    private UserService userService;

    @Mock
    private LikeService likeService;

    @Mock
    private CommentService commentService;

    @Mock
    private FriendshipService friendshipService;

    @Mock
    private BadWordsFilterService badWordsFilterService;

    @InjectMocks
    private PostControllerMVC postControllerMVC;

    @Mock
    private Model model;

    @Mock
    private RedirectAttributes redirectAttributes;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindAll() {
        Member authenticatedMember = new Member();
        authenticatedMember.setId(1L);

        List<FriendshipInvitation> friends = new ArrayList<>();
        FriendshipInvitation friendInvitation = new FriendshipInvitation();
        Member friend = new Member();
        friend.setId(2L);
        friendInvitation.setInvitingMember(authenticatedMember);
        friendInvitation.setAcceptingMember(friend);
        friends.add(friendInvitation);

        Post post = new Post();
        post.setMember(friend);
        List<Post> posts = new ArrayList<>();
        posts.add(post);

        when(memberService.getAuthenticatedMember()).thenReturn(authenticatedMember);
        when(friendshipService.findFriendsAccepted(authenticatedMember)).thenReturn(friends);
        when(postService.findAll()).thenReturn(posts);
        when(likeService.hasMemberLikedPost(authenticatedMember, post)).thenReturn(true);

        String viewName = postControllerMVC.findAll(model);

        assertEquals("posts-of-friends", viewName);
        verify(model).addAttribute(eq("likedFriendsPosts"), any(Map.class));
        verify(model).addAttribute("authenticatedMember", authenticatedMember);
        verify(model).addAttribute("posts", posts);
    }

    @Test
    public void testFindAllYour() {
        Member authenticatedMember = new Member();
        authenticatedMember.setId(1L);
        Post post = new Post();
        List<Post> posts = new ArrayList<>();
        posts.add(post);

        authenticatedMember.setPosts(posts);

        when(memberService.getAuthenticatedMember()).thenReturn(authenticatedMember);
        when(likeService.hasMemberLikedPost(authenticatedMember, post)).thenReturn(true);

        String viewName = postControllerMVC.findAllYour(model);

        assertEquals("posts-your", viewName);
        verify(model).addAttribute(eq("likedYourPosts"), any(Map.class));
        verify(model).addAttribute("authenticatedMember", authenticatedMember);
        verify(model).addAttribute("myPosts", posts);
    }

    @Test
    public void testFindAllByMember() {
        Member member = new Member();
        List<Post> posts = new ArrayList<>();

        when(memberService.getAuthenticatedMember()).thenReturn(member);
        when(postService.findAllByMember(member)).thenReturn(posts);

        String viewName = postControllerMVC.findAllByMember(model);

        assertEquals("posts-your", viewName);
        verify(model).addAttribute("posts", posts);
    }

    @Test
    public void testGetPostById() {
        Member member = new Member();
        Post post = new Post();
        List<Comment> comments = new ArrayList<>();
        Comment comment = new Comment();
        comments.add(comment);
        post.setComments(comments);

        when(memberService.getAuthenticatedMember()).thenReturn(member);
        when(postService.findById(1L)).thenReturn(post);
        when(likeService.hasMemberLikedPost(member, post)).thenReturn(true);
        when(likeService.hasMemberLikedComment(member, comment)).thenReturn(true);

        String viewName = postControllerMVC.getPostById(1L, model);

        assertEquals("post-info", viewName);
        verify(model).addAttribute("member", member);
        verify(model).addAttribute("post", post);
        verify(model).addAttribute(eq("likedYourPosts"), any(Map.class));
        verify(model).addAttribute(eq("likedComments"), any(Map.class));
    }

    @Test
    public void testShowPostForm() {
        Member member = new Member();

        when(memberService.getAuthenticatedMember()).thenReturn(member);

        String viewName = postControllerMVC.showPostForm(model);

        assertEquals("post-form", viewName);
        verify(model).addAttribute(eq("post"), any(Post.class));
    }

    @Test
    public void testShowPostFormUpdate() {
        Member member = new Member();
        Post post = new Post();
        post.setMember(member);

        when(memberService.getAuthenticatedMember()).thenReturn(member);
        when(postService.findById(1L)).thenReturn(post);

        String viewName = postControllerMVC.showPostFormUpdate(1L, model);

        assertEquals("post-form", viewName);
        verify(model).addAttribute("post", post);
    }

    @Test
    public void testSavePost() {
        Member member = new Member();
        Post post = new Post();
        post.setContent("Some content");
        post.setMediaUrl("");

        when(memberService.getAuthenticatedMember()).thenReturn(member);
        when(badWordsFilterService.filterObsceneLanguage(anyString())).thenReturn("Filtered content");
        when(postService.getRandomMediaUrl()).thenReturn("random-media-url");

        String viewName = postControllerMVC.savePost(post, redirectAttributes);

        assertEquals("redirect:/mvc/posts/?postId=" + post.getId(), viewName);
        verify(postService).save(post);
        verify(redirectAttributes).addFlashAttribute("success", "Post has been created.");
    }

    @Test
    public void testUpdatePost() {
        Member member = new Member();
        Post post = new Post();
        post.setContent("Updated content");

        Post existingPost = new Post();
        existingPost.setId(1L);

        when(memberService.getAuthenticatedMember()).thenReturn(member);
        when(postService.findById(post.getId())).thenReturn(existingPost);
        when(badWordsFilterService.filterObsceneLanguage(anyString())).thenReturn("Filtered content");

        String viewName = postControllerMVC.updatePost(post, redirectAttributes);

        assertEquals("redirect:/mvc/posts/?postId=" + post.getId(), viewName);
        verify(postService).save(existingPost);
        verify(redirectAttributes).addFlashAttribute("success", "Post has been updated.");
    }

    @Test
    public void testDeletePost() {
        String viewName = postControllerMVC.deletePost(1L, redirectAttributes);

        assertEquals("redirect:/mvc/posts-your", viewName);
        verify(postService).deleteById(1L);
        verify(redirectAttributes).addFlashAttribute("success", "Post has been deleted.");
    }
}
