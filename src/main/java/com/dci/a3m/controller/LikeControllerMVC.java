package com.dci.a3m.controller;

import com.dci.a3m.entity.Comment;
import com.dci.a3m.entity.Like;
import com.dci.a3m.entity.Member;
import com.dci.a3m.entity.Post;
import com.dci.a3m.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/mvc")
public class LikeControllerMVC {

    private final MemberService memberService;
    private final PostService postService;
    private final LikeService likeService;
    private final CommentService commentService;
    private final EmailService emailService;

    @Autowired
    public LikeControllerMVC(MemberService memberService, PostService postService, LikeService likeService, CommentService commentService, EmailService emailService) {
        this.memberService = memberService;
        this.postService = postService;
        this.likeService = likeService;
        this.commentService = commentService;
        this.emailService = emailService;
    }

    // POSTS
    // LIKE POST FROM THE POST-INFO
    @PostMapping("/like-post-info")
    public String likePostInfo(@RequestParam("postId") Long id) {
        // Currently logged in member
        Member authenticatedMember = memberService.getAuthenticatedMember();
        if (authenticatedMember == null) {
            return "member-error";
        }
        // Find post
        Post post = postService.findById(id);
        if (post == null) {
            return "post-error";
        }

        // Check if member has already liked the post
        Like existingLike = likeService.findByMemberAndPost(authenticatedMember, post);
        if (existingLike != null) {
            return "redirect:/mvc/posts/?postId=" + id;
        }

        // Like the post
        Like like = new Like(authenticatedMember, post);
        post.getLikes().add(like);
        likeService.save(like);

        // Send an email to the member who posted the post
        Member member = post.getMember();
        emailService.sendLikeEmail(member, authenticatedMember);

        return "redirect:/mvc/posts/?postId=" + id;
    }

    // UNLIKE POST FROM THE POST-INFO
    @PostMapping("/unlike-post-info")
    public String unlikePostInfo(@RequestParam("postId") Long id) {
        // Currently logged in member
        Member authenticatedMember = memberService.getAuthenticatedMember();
        if (authenticatedMember == null) {
            return "member-error";
        }

        Post post = postService.findById(id);
        if (post == null) {
            return "post-error";
        }

        Like like = likeService.findByMemberAndPost(authenticatedMember, post);
        if (like == null) {
            return "redirect:/mvc/posts/?postId=" + id;
        }

        post.getLikes().remove(like);
        likeService.deleteById(like.getId());
        return "redirect:/mvc/posts/?postId=" + id;
    }

    // LIKE POST FROM OWN PROFILE
    @PostMapping("/like-post-own-profile")
    public String likePostProfile(@RequestParam("postId") Long postId) {
        // Currently logged in member
        Member authenticatedMember = memberService.getAuthenticatedMember();

        // who is the member of the post with the postId
        Long memberId = postService.findById(postId).getMember().getId();

        if (authenticatedMember == null) {
            return "member-error";
        }
        // Find post
        Post post = postService.findById(postId);

        if (post == null) {
            return "post-error";
        }

        // Check if member has already liked the post
        Like existingLike = likeService.findByMemberAndPost(authenticatedMember, post);
        if (existingLike != null) {
            return "redirect:/mvc/members/?memberId=" + memberId;
        }

        // Like the post
        Like like = new Like(authenticatedMember, post);
        post.getLikes().add(like);
        likeService.save(like);

        // Send an email to the member who posted the post
        Member member = post.getMember();
        emailService.sendLikeEmail(member, authenticatedMember);
        return "redirect:/mvc/members/?memberId=" + memberId;
    }

    // UNLIKE POST FROM OWN PROFILE
    @PostMapping("/unlike-post-own-profile")
    public String unlikePostProfile(@RequestParam("postId") Long postId) {
        // Currently logged in member
        Member authenticatedMember = memberService.getAuthenticatedMember();

        // who is the member of the post with the postId
        Long memberId = postService.findById(postId).getMember().getId();

        if (authenticatedMember == null) {
            return "member-error";
        }

        Post post = postService.findById(postId);
        if (post == null) {
            return "post-error";
        }

        Like like = likeService.findByMemberAndPost(authenticatedMember, post);
        if (like == null) {
            return "redirect:/mvc/members/?memberId=" + memberId;
        }

        post.getLikes().remove(like);
        likeService.deleteById(like.getId());
        return "redirect:/mvc/members/?memberId=" + memberId;
    }

    // LIKE POST of friend
    @PostMapping("/like-post-of-friend")
    public String likePostOfFriend(@RequestParam("postId") Long postId) {
        // Currently logged in member
        Member authenticatedMember = memberService.getAuthenticatedMember();



        if (authenticatedMember == null) {
            return "member-error";
        }
        // Find post
        Post post = postService.findById(postId);

        if (post == null) {
            return "post-error";
        }

        // Check if member has already liked the post
        Like existingLike = likeService.findByMemberAndPost(authenticatedMember, post);
        if (existingLike != null) {
            return "redirect:/mvc/members/?memberId=" + authenticatedMember.getId();
        }

        // Like the post
        Like like = new Like(authenticatedMember, post);
        post.getLikes().add(like);
        likeService.save(like);

        // Send an email to the member who posted the post
        Member member = post.getMember();
        emailService.sendLikeEmail(member, authenticatedMember);
        return "redirect:/mvc/members/?memberId=" + authenticatedMember.getId();
    }

    // UNLIKE POST of friend
    @PostMapping("/unlike-post-of-friend")
    public String unlikePostOfFriend(@RequestParam("postId") Long postId) {
        // Currently logged in member
        Member authenticatedMember = memberService.getAuthenticatedMember();

        // who is the member of the post with the postId
        Long memberId = postService.findById(postId).getMember().getId();

        if (authenticatedMember == null) {
            return "member-error";
        }

        Post post = postService.findById(postId);
        if (post == null) {
            return "post-error";
        }

        Like like = likeService.findByMemberAndPost(authenticatedMember, post);
        if (like == null) {
            return "redirect:/mvc/members/?memberId=" + authenticatedMember.getId();
        }

        post.getLikes().remove(like);
        likeService.deleteById(like.getId());
        return "redirect:/mvc/members/?memberId=" + authenticatedMember.getId();
    }



    // LIKE POST of friend
    @PostMapping("/like-post-of-friend2")
    public String likePostOfFriend2(@RequestParam("postId") Long postId) {
        // Currently logged in member
        Member authenticatedMember = memberService.getAuthenticatedMember();

        // who is the member of the post with the postId
        Long memberId = postService.findById(postId).getMember().getId();

        if (authenticatedMember == null) {
            return "member-error";
        }
        // Find post
        Post post = postService.findById(postId);
        if (post == null) {
            return "post-error";
        }

        // Check if member has already liked the post
        Like existingLike = likeService.findByMemberAndPost(authenticatedMember, post);
        if (existingLike != null) {
            return "redirect:/mvc/members/?memberId=" + memberId;
        }

        // Like the post
        Like like = new Like(authenticatedMember, post);
        post.getLikes().add(like);
        likeService.save(like);

        // Send an email to the member who posted the post
        Member member = post.getMember();
        emailService.sendLikeEmail(member, authenticatedMember);
        return "redirect:/mvc/members/?memberId=" + memberId;
    }

    // UNLIKE POST of friend
    @PostMapping("/unlike-post-of-friend2")
    public String unlikePostOfFriend2(@RequestParam("postId") Long postId) {
        // Currently logged in member
        Member authenticatedMember = memberService.getAuthenticatedMember();

        // who is the member of the post with the postId
        Long memberId = postService.findById(postId).getMember().getId();

        if (authenticatedMember == null) {
            return "member-error";
        }

        Post post = postService.findById(postId);
        if (post == null) {
            return "post-error";
        }

        Like like = likeService.findByMemberAndPost(authenticatedMember, post);
        if (like == null) {
            return "redirect:/mvc/members/?memberId=" + memberId;
        }

        post.getLikes().remove(like);
        likeService.deleteById(like.getId());
        return "redirect:/mvc/members/?memberId=" + memberId;
    }





    // LIKE POST FROM FRIENDS POSTS VIEW
    @PostMapping("/like-post-friend")
    public String likePostFriends(@RequestParam("postId") Long id) {
        // Currently logged in member
        Member authenticatedMember = memberService.getAuthenticatedMember();
        if (authenticatedMember == null) {
            return "member-error";
        }
        // Find post
        Post post = postService.findById(id);
        if (post == null) {
            return "post-error";
        }

        // Check if member has already liked the post
        Like existingLike = likeService.findByMemberAndPost(authenticatedMember, post);
        if (existingLike != null) {
            return "redirect:/mvc/posts-of-friends";
        }

        // Like the post
        Like like = new Like(authenticatedMember, post);
        post.getLikes().add(like);
        likeService.save(like);

        // Send an email to the member who posted the post
        Member member = post.getMember();
        emailService.sendLikeEmail(member, authenticatedMember);
        return "redirect:/mvc/posts-of-friends";
    }

    // UNLIKE POST FROM FRIENDS POSTS VIEW
    @PostMapping("/unlike-post-friend")
    public String unlikePostFriends(@RequestParam("postId") Long id) {
        // Currently logged in member
        Member authenticatedMember = memberService.getAuthenticatedMember();
        if (authenticatedMember == null) {
            return "member-error";
        }

        Post post = postService.findById(id);
        if (post == null) {
            return "post-error";
        }

        Like like = likeService.findByMemberAndPost(authenticatedMember, post);
        if (like == null) {
            return "redirect:/mvc/posts-of-friends";
        }

        post.getLikes().remove(like);
        likeService.deleteById(like.getId());
        return "redirect:/mvc/posts-of-friends";
    }

    // LIKE POST FROM YOUR POSTS VIEW
    @PostMapping("/like-post-your")
    public String likePostYour(@RequestParam("postId") Long id) {
        // Currently logged in member
        Member authenticatedMember = memberService.getAuthenticatedMember();
        if (authenticatedMember == null) {
            return "member-error";
        }
        // Find post
        Post post = postService.findById(id);
        if (post == null) {
            return "post-error";
        }

        // Check if member has already liked the post
        Like existingLike = likeService.findByMemberAndPost(authenticatedMember, post);
        if (existingLike != null) {
            return "redirect:/mvc/posts-your";
        }

        // Like the post
        Like like = new Like(authenticatedMember, post);
        post.getLikes().add(like);
        likeService.save(like);

        // Send an email to the member who posted the post
        Member member = post.getMember();
        emailService.sendLikeEmail(member, authenticatedMember);
        return "redirect:/mvc/posts-your";
    }

    // UNLIKE POST FROM YOUR POSTS VIEW
    @PostMapping("/unlike-post-your")
    public String unlikePostYour(@RequestParam("postId") Long id) {
        // Currently logged in member
        Member authenticatedMember = memberService.getAuthenticatedMember();
        if (authenticatedMember == null) {
            return "member-error";
        }

        Post post = postService.findById(id);
        if (post == null) {
            return "post-error";
        }

        Like like = likeService.findByMemberAndPost(authenticatedMember, post);
        if (like == null) {
            return "redirect:/mvc/posts-your";
        }

        post.getLikes().remove(like);
        likeService.deleteById(like.getId());
        return "redirect:/mvc/posts-your";
    }

    // COMMENTS

    // LIKE COMMENT
    @PostMapping("/like-comment")
    public String likeComment(@RequestParam("commentId") Long id) {
        // Currently logged in member
        Member authenticatedMember = memberService.getAuthenticatedMember();
        if (authenticatedMember == null) {
            return "member-error";
        }

        //Find comment
        Comment comment = commentService.findById(id);
        if (comment == null) {
            return "comment-error";
        }

        // Check if member has liked the comment
        Like existinLike = likeService.findByMemberAndComment(authenticatedMember, comment);
        if (existinLike != null) {
            return "redirect:/mvc/posts/?postId=" + comment.getPost().getId();
        }

        // Like the comment
        Like like = new Like(authenticatedMember, comment);
        comment.getLikes().add(like);
        likeService.save(like);

        // Send an email to the member who posted the comment
        Member member = comment.getMember();
        emailService.sendLikeCommentEmail(member, authenticatedMember);
        return "redirect:/mvc/posts/?postId=" + comment.getPost().getId();
    }

    // UNLIKE COMMENT
    @PostMapping("/unlike-comment")
    public String unlikeComment(@RequestParam("commentId") Long id) {
        // Currently logged in member
        Member authenticatedMember = memberService.getAuthenticatedMember();
        if (authenticatedMember == null) {
            return "member-error";
        }
        //Find the comment
        Comment comment = commentService.findById(id);
        if (comment == null) {
            return "comment-error";
        }

        Like like = likeService.findByMemberAndComment(authenticatedMember, comment);
        if (like == null) {
            return "redirect:/mvc/posts/?postId=" + comment.getPost().getId();
        }

        comment.getLikes().remove(like);
        likeService.deleteById(like.getId());
        return "redirect:/mvc/posts/?postId=" + comment.getPost().getId();
    }
}
