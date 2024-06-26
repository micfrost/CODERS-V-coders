package com.dci.a3m.ControllerMVCTest;


import com.dci.a3m.controller.CommentControllerMVC;
import com.dci.a3m.entity.Comment;
import com.dci.a3m.entity.Member;
import com.dci.a3m.entity.Post;
import com.dci.a3m.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import java.util.ArrayList;
import java.util.List;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class CommentControllerMVCTest {

    @Mock
    private UserDetailsManager userDetailsManager;

    @Mock
    private CommentService commentService;

    @Mock
    private MemberService memberService;

    @Mock
    private PostService postService;

    @Mock
    private UserService userService;

    @Mock
    private BadWordsFilterService badWordsFilterService;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private CommentControllerMVC commentControllerMVC;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/views/");
        viewResolver.setSuffix(".jsp");

        mockMvc = MockMvcBuilders.standaloneSetup(commentControllerMVC)
                .setViewResolvers(viewResolver)
                .build();
    }

    @Test
    void findAll() throws Exception {
        List<Comment> comments = new ArrayList<>();
        comments.add(new Comment());
        when(commentService.findAll()).thenReturn(comments);

        mockMvc.perform(get("/mvc/comments"))
                .andExpect(status().isOk())
                .andExpect(view().name("comments"))
                .andExpect(model().attributeExists("comments"));

        verify(commentService, times(1)).findAll();
    }

    @Test
    void findAllByMember() throws Exception {
        Member member = new Member();
        List<Comment> comments = new ArrayList<>();
        comments.add(new Comment());
        when(memberService.getAuthenticatedMember()).thenReturn(member);
        when(commentService.findAllByMember(member)).thenReturn(comments);

        mockMvc.perform(get("/mvc/comments/member"))
                .andExpect(status().isOk())
                .andExpect(view().name("comments"))
                .andExpect(model().attributeExists("comments"));

        verify(commentService, times(1)).findAllByMember(member);
    }

    @Test
    void findAllByMember_NotFound() throws Exception {
        when(memberService.getAuthenticatedMember()).thenReturn(null);

        mockMvc.perform(get("/mvc/comments/member"))
                .andExpect(status().isOk())
                .andExpect(view().name("member-error"))
                .andExpect(model().attributeExists("error"));

        verify(memberService, times(1)).getAuthenticatedMember();
    }

    @Test
    void getCommentById() throws Exception {
        Comment comment = new Comment();
        comment.setId(1L);
        when(commentService.findById(1L)).thenReturn(comment);

        mockMvc.perform(get("/mvc/comments/")
                        .param("commentId", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("comment-info"))
                .andExpect(model().attributeExists("comment"));

        verify(commentService, times(1)).findById(1L);
    }

    @Test
    void getCommentById_NotFound() throws Exception {
        when(commentService.findById(1L)).thenReturn(null);

        mockMvc.perform(get("/mvc/comments/")
                        .param("commentId", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("comment-error"))
                .andExpect(model().attributeExists("error"));

        verify(commentService, times(1)).findById(1L);
    }

    @Test
    void showCommentForm() throws Exception {
        Member member = new Member();
        when(memberService.getAuthenticatedMember()).thenReturn(member);

        mockMvc.perform(get("/mvc/comment-form")
                        .param("postId", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("comment-form"))
                .andExpect(model().attributeExists("postId"))
                .andExpect(model().attributeExists("comment"));

        verify(memberService, times(1)).getAuthenticatedMember();
    }

    @Test
    void showCommentFormUpdate() throws Exception {
        Comment comment = new Comment();
        Post post = new Post();
        post.setId(1L);
        comment.setPost(post);
        when(commentService.findById(1L)).thenReturn(comment);

        mockMvc.perform(get("/mvc/comment-form-update")
                        .param("commentId", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("comment-form"))
                .andExpect(model().attributeExists("postId"))
                .andExpect(model().attributeExists("comment"));

        verify(commentService, times(1)).findById(1L);
    }

    @Test
    void saveComment() throws Exception {
        Member authenticatedMember = new Member();
        Member postOwner = new Member();
        Post post = new Post();
        post.setId(1L);
        post.setMember(postOwner);
        Comment comment = new Comment();
        comment.setContent("Test content");

        // Ensure the memberService mock returns a Member object
        when(memberService.getAuthenticatedMember()).thenReturn(authenticatedMember);
        when(postService.findById(1L)).thenReturn(post);
        when(badWordsFilterService.filterObsceneLanguage("Test content")).thenReturn("Filtered content");

        mockMvc.perform(post("/mvc/comment-form/create")
                        .param("postId", "1")
                        .flashAttr("comment", comment))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/mvc/posts/?postId=1"));

        verify(commentService, times(1)).save(any(Comment.class));
        verify(emailService, times(1)).sendCommentEmail(eq(postOwner), eq(authenticatedMember));
    }


    @Test
    void updateComment() throws Exception {
        Comment comment = new Comment();
        comment.setId(1L);
        comment.setContent("Updated content");
        Post post = new Post();
        post.setId(1L);
        comment.setPost(post);

        when(commentService.findById(1L)).thenReturn(comment);
        when(badWordsFilterService.filterObsceneLanguage("Updated content")).thenReturn("Filtered content");

        mockMvc.perform(post("/mvc/comment-form/update")
                        .flashAttr("comment", comment))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/mvc/posts/?postId=1"));

        verify(commentService, times(1)).save(any(Comment.class));
    }

    @Test
    void deleteComment() throws Exception {
        Comment comment = new Comment();
        Post post = new Post();
        post.setId(1L);
        comment.setPost(post);

        when(commentService.findById(1L)).thenReturn(comment);

        mockMvc.perform(get("/mvc/comment-delete")
                        .param("commentId", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/mvc/posts/?postId=1"));

        verify(commentService, times(1)).deleteById(1L);
    }
}
