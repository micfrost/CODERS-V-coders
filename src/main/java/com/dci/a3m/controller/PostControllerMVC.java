package com.dci.a3m.controller;

import com.dci.a3m.entity.*;
import com.dci.a3m.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/mvc")
public class PostControllerMVC {

    private final PostService postService;
    private final MemberService memberService;
    private final UserService userService;
    private final LikeService likeService;
    private final CommentService commentService;
    private final FriendshipService friendshipService;
    private final BadWordsFilterService badWordsFilterService;

    @Autowired
    public PostControllerMVC(PostService postService, MemberService memberService, UserService userService, LikeService likeService, CommentService commentService, FriendshipService friendshipService, BadWordsFilterService badWordsFilterService) {
        this.postService = postService;
        this.memberService = memberService;
        this.userService = userService;
        this.likeService = likeService;
        this.commentService = commentService;
        this.friendshipService = friendshipService;
        this.badWordsFilterService = badWordsFilterService;
    }

    // CRUD OPERATIONS

    // READ ALL
    @GetMapping("/posts-of-friends")
    public String findAll(Model model) {
        Member authenticatedMember = memberService.getAuthenticatedMember();
        if (authenticatedMember == null) {
            model.addAttribute("error", "Member not found.");
            return "member-error";
        }

        // Prepare attributes with friends accepted for Thymeleaf
        List<FriendshipInvitation> friends = friendshipService.findFriendsAccepted(authenticatedMember);
        List<Long> friendIds = friends.stream()
                .map(friend -> friend.getInvitingMember().getId().equals(authenticatedMember.getId()) ? friend.getAcceptingMember().getId() : friend.getInvitingMember().getId())
                .collect(Collectors.toList());

        List<Post> posts = postService.findAll().stream()
                .filter(post -> friendIds.contains(post.getMember().getId()))
                .collect(Collectors.toList());

        // desc order
        posts.sort((p1, p2) -> p2.getCreatedAt().compareTo(p1.getCreatedAt()));


        Map<Long, Boolean> likedFriendsPosts = new HashMap<>();
        for (Post post : posts) {
            boolean liked = likeService.hasMemberLikedPost(authenticatedMember, post);
            likedFriendsPosts.put(post.getId(), liked);
        }

        // Friendship Invitations
        List<FriendshipInvitation> invitations = friendshipService.findByAcceptingMemberAndNotAccepted(authenticatedMember);
        model.addAttribute("invitations", invitations);

        // desc order invitations
        invitations.sort((i1, i2) -> i2.getId().compareTo(i1.getId()));



        model.addAttribute("likedFriendsPosts", likedFriendsPosts);
        model.addAttribute("authenticatedMember", authenticatedMember);
        model.addAttribute("posts", posts);
        model.addAttribute("invitations", invitations);
        return "posts-of-friends";
    }

    // READ ALL
    @GetMapping("/posts-your")
    public String findAllYour(Model model) {
        Member authenticatedMember = memberService.getAuthenticatedMember();
        if (authenticatedMember == null) {
            model.addAttribute("error", "Member not found.");
            return "member-error";
        }

        List<Post> myPosts = authenticatedMember.getPosts();

        // desc order
        myPosts.sort((p1, p2) -> p2.getCreatedAt().compareTo(p1.getCreatedAt()));


        Map<Long, Boolean> likedYourPosts = new HashMap<>();
        for (Post post : myPosts) {
            boolean liked = likeService.hasMemberLikedPost(authenticatedMember, post);
            likedYourPosts.put(post.getId(), liked);
        }


        model.addAttribute("likedYourPosts", likedYourPosts);
        model.addAttribute("authenticatedMember", authenticatedMember);
        model.addAttribute("myPosts", myPosts);

        return "posts-your";
    }

    // READ ALL POST OF CURRENTLY LOGGED IN MEMBER
    @GetMapping("/posts/member")
    public String findAllByMember(Model model) {
        // Currently logged in member
        Member member = memberService.getAuthenticatedMember();

        if (member == null) {
            model.addAttribute("error", "Member not found.");
            return "member-error";
        }


        List<Post> posts = postService.findAllByMember(member);
        model.addAttribute("posts", posts);
        return "posts-your";
    }

    // READ BY ID
    @GetMapping("/posts/")
    public String getPostById(@RequestParam("postId") Long id, Model model) {
        Member member = memberService.getAuthenticatedMember();
        Post post = postService.findById(id);
        if (post == null) {
            model.addAttribute("error", "Post not found.");
            return "post-error";
        }

        boolean liked = likeService.hasMemberLikedPost(member, post);
        Map<Long, Boolean> likedYourPosts = new HashMap<>();
        likedYourPosts.put(post.getId(), liked);

        Map<Long, Boolean> likedComments = new HashMap<>();
        for (Comment comment : post.getComments()) {
            boolean likedComment = likeService.hasMemberLikedComment(member, comment);
            likedComments.put(comment.getId(), likedComment);
        }

        model.addAttribute("member", member);
        model.addAttribute("post", post);
        model.addAttribute("likedYourPosts", likedYourPosts);
        model.addAttribute("likedComments", likedComments);
        return "post-info";
    }


    // CREATE POST - SHOW FORM
    @GetMapping("/post-form")
    public String showPostForm(Model model) {
        // show the form only to currently logged in member
        Member member = memberService.getAuthenticatedMember();
        if (member == null) {
            model.addAttribute("error", "Member not found.");
            return "member-error";
        }

        model.addAttribute("post", new Post());
        return "post-form";
    }

    // UPDATE POST - SHOW FORM
    @GetMapping("/post-form-update")
    public String showPostFormUpdate(@RequestParam("postId") Long id, Model model) {
        Member member = memberService.getAuthenticatedMember();
        Post post = postService.findById(id);
        if (post == null || !post.getMember().equals(member)) {
            model.addAttribute("error", "Post not found.");
            return "post-error";
        }
        model.addAttribute("post", post);
        return "post-form";
    }


    // SAVE POST
    @PostMapping("/post-form/create")
    public String savePost(@ModelAttribute("post") Post post, RedirectAttributes redirectAttributes) {
        // Currently logged in member
        Member member = memberService.getAuthenticatedMember();
        if (member == null) {
            return "member-error";
        }

        // Filter out obscene language
        String filteredContent = badWordsFilterService.filterObsceneLanguage(post.getContent());
        post.setContent(filteredContent);

        if (post.getMediaUrl().isEmpty()) {
            post.setMediaUrl(postService.getRandomMediaUrl());
        }

        post.setMember(member);
        postService.save(post);
        redirectAttributes.addFlashAttribute("success", "Post has been created.");
        return "redirect:/mvc/posts/?postId=" + post.getId();
    }

    // UPDATE POST
    @PostMapping("/post-form/update")
    public String updatePost(@ModelAttribute("post") Post post, RedirectAttributes redirectAttributes) {
        // Currently logged in member
        Member member = memberService.getAuthenticatedMember();
        if (member == null) {
            return "member-error";
        }
        // Find existing post
        Post existingPost = postService.findById(post.getId());

        // Filter out obscene language
        String filteredContent = badWordsFilterService.filterObsceneLanguage(post.getContent());
        post.setContent(filteredContent);

        // Update post
        existingPost.setContent(post.getContent());
        existingPost.setBackground(post.getBackground());
        existingPost.setMediaUrl(post.getMediaUrl());

        postService.save(existingPost);
        redirectAttributes.addFlashAttribute("success", "Post has been updated.");
        return "redirect:/mvc/posts/?postId=" + post.getId();
    }

    // DELETE POST
    @GetMapping("/post-delete")
    public String deletePost(@RequestParam("postId") Long id, RedirectAttributes redirectAttributes) {
        postService.deleteById(id);
        redirectAttributes.addFlashAttribute("success", "Post has been deleted.");
        return "redirect:/mvc/posts-your";
    }


}
