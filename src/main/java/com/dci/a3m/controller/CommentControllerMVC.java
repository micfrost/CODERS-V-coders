package com.dci.a3m.controller;


import com.dci.a3m.entity.Comment;
import com.dci.a3m.entity.Member;
import com.dci.a3m.entity.Post;
import com.dci.a3m.entity.User;
import com.dci.a3m.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/mvc")
public class CommentControllerMVC {
    private final UserDetailsManager userDetailsManager;
    private final CommentService commentService;
    private final MemberService memberService;
    private final  PostService postService;
    private final UserService userService;
    private final BadWordsFilterService badWordsFilterService;
    private final EmailService emailService;

    @Autowired
    public CommentControllerMVC(UserDetailsManager userDetailsManager, CommentService commentService, MemberService memberService, PostService postService, UserService userService, BadWordsFilterService badWordsFilterService, EmailService emailService) {
        this.userDetailsManager = userDetailsManager;
        this.commentService = commentService;
        this.memberService = memberService;
        this.postService = postService;
        this.userService = userService;
        this.badWordsFilterService = badWordsFilterService;
        this.emailService = emailService;
    }
    // CRUD OPERATIONS

    // READ ALL
    @GetMapping("/comments")
    public String findAll(Model model) {
        List<Comment> comments = commentService.findAll();
        model.addAttribute("comments", comments);
        return "comments";
    }

    // READ ALL COMMENTS OF CURRENTLY LOGGED IN MEMBER
    @GetMapping("/comments/member")
    public String findAllByMember(Model model) {
        Member authenticatedMember = memberService.getAuthenticatedMember();

        if (authenticatedMember == null) {
            model.addAttribute("error", "Member not found.");
            return "member-error";
        }

        List<Comment> comments = commentService.findAllByMember(authenticatedMember);
        model.addAttribute("comments", comments);
        return "comments";
    }

    // READ BY ID
    @GetMapping("/comments/")
    public String getCommentById(@RequestParam("commentId") Long id, Model model) {
        Comment comment = commentService.findById(id);
        if (comment == null) {
            model.addAttribute("error", "Comment not found.");
            return "comment-error";
        }
        model.addAttribute("comment", comment);
        return "comment-info";
    }

    // CREATE COMMENT - SHOW FORM
    @GetMapping("/comment-form")
    public String showCommentForm(@RequestParam("postId") Long postId, Model model) {
        Member authenticatedMember = memberService.getAuthenticatedMember();
        if (authenticatedMember == null) {
            model.addAttribute("error", "Member not found.");
            return "member-error";
        }
        model.addAttribute("postId", postId);
        model.addAttribute("comment", new Comment());
        return "comment-form";
    }

    // UPDATE COMMENT - SHOW FORM
    @GetMapping("/comment-form-update")
    public String showCommentFormUpdate(@RequestParam("commentId") Long id, Model model) {
        Comment comment = commentService.findById(id);
        if (comment == null) {
            model.addAttribute("error", "Comment not found.");
            return "comment-error";
        }
        Post post = comment.getPost();
        model.addAttribute("postId", post.getId());
        model.addAttribute("comment", comment);
        return "comment-form";
    }

    // SAVE COMMENT
    @PostMapping("/comment-form/create")
    public String saveComment(@RequestParam("postId") Long postId, Comment comment, RedirectAttributes redirectAttributes) {
        Member authenticatedMember = memberService.getAuthenticatedMember();
        if (authenticatedMember == null) {
            return "comment-error";
        }
        Post post = postService.findById(postId);
        // Filter out obscene language
        String filteredContent = badWordsFilterService.filterObsceneLanguage(comment.getContent());
        comment.setContent(filteredContent);

        comment.setPost(post);
        comment.setMember(authenticatedMember);
        commentService.save(comment);

        // Send a notification to the post owner
        Member postOwner = post.getMember();
        emailService.sendCommentEmail(postOwner, authenticatedMember);

        redirectAttributes.addFlashAttribute("success", "Comment has been created.");
        return "redirect:/mvc/posts/?postId=" + postId;
    }

    // UPDATE COMMENT
    @PostMapping("/comment-form/update")
    public String updateComment(@ModelAttribute("comment") Comment comment, RedirectAttributes redirectAttributes) {
        Comment existingComment = commentService.findById(comment.getId());
        if (existingComment == null) {
            return "comment-error";
        }
        // Filter out obscene language
        String filteredContent = badWordsFilterService.filterObsceneLanguage(comment.getContent());
        comment.setContent(filteredContent);

        existingComment.setContent(comment.getContent());
        commentService.save(existingComment);
        redirectAttributes.addFlashAttribute("success", "Comment has been updated.");
        return "redirect:/mvc/posts/?postId=" + existingComment.getPost().getId();
    }

    // DELETE COMMENT
    @GetMapping("/comment-delete")
    public String deleteComment(@RequestParam("commentId") Long id, RedirectAttributes redirectAttributes) {
        Comment comment = commentService.findById(id);
        commentService.deleteById(id);
        redirectAttributes.addFlashAttribute("success", "Comment has been deleted.");
        return "redirect:/mvc/posts/?postId=" + comment.getPost().getId();
    }
}