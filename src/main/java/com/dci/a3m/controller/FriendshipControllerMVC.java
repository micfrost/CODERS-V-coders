package com.dci.a3m.controller;

import com.dci.a3m.entity.FriendshipInvitation;
import com.dci.a3m.entity.Member;
import com.dci.a3m.entity.Post;
import com.dci.a3m.entity.User;
import com.dci.a3m.service.EmailService;
import com.dci.a3m.service.FriendshipService;
import com.dci.a3m.service.MemberService;
import com.dci.a3m.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/mvc")
public class FriendshipControllerMVC {

    private final FriendshipService friendshipService;
    private final MemberService memberService;
    private final PostService postService;
    private final EmailService emailService;

    @Autowired
    public FriendshipControllerMVC(FriendshipService friendshipService, MemberService memberService, PostService postService, EmailService emailService) {
        this.friendshipService = friendshipService;
        this.memberService = memberService;
        this.postService = postService;

        this.emailService = emailService;
    }


    // CRUD OPERATIONS

    // READ

    // READ ALL FRIENDSHIP INVITATIONS FOR CURRENTLY LOGGED IN MEMBER THAT ARE NOT ACCEPTED YET
    @GetMapping("/friendship-invitations")
    public String showFriendshipInvitations(Model model) {

        Member acceptingMember = memberService.getAuthenticatedMember();

        List<FriendshipInvitation> invitations = friendshipService.findByAcceptingMemberAndNotAccepted(acceptingMember);

        model.addAttribute("invitations", invitations);

        return "friendship-invitations";
    }

    @GetMapping("/friends")
    public String showFriends(Model model) {

        Member member = memberService.getAuthenticatedMember();
        List<FriendshipInvitation> friendsAcceptedAndNotAccepted = friendshipService.findFriendsAccepted(member);
        List<FriendshipInvitation> friends= friendsAcceptedAndNotAccepted.stream().filter(friend -> friend.isAccepted()).collect(Collectors.toList());



        // Prepare attributes for Thymeleaf
        List<Map<String, Object>> friendDetails = friends.stream().map(friend -> {
            Map<String, Object> details = new HashMap<>();
            details.put("friendshipId", friend.getId());
            if (friend.getInvitingMember().getId().equals(member.getId())) {
                details.put("profilePicture", friend.getAcceptingMember().getProfilePicture());
                details.put("firstName", friend.getAcceptingMember().getFirstName());
                details.put("lastName", friend.getAcceptingMember().getLastName());
                details.put("username", friend.getAcceptingMember().getUser().getUsername());
                details.put("memberId", friend.getAcceptingMember().getId());
            } else {
                details.put("profilePicture", friend.getInvitingMember().getProfilePicture());
                details.put("firstName", friend.getInvitingMember().getFirstName());
                details.put("lastName", friend.getInvitingMember().getLastName());
                details.put("username", friend.getInvitingMember().getUser().getUsername());
                details.put("memberId", friend.getInvitingMember().getId());
            }
            return details;
        }).collect(Collectors.toList());

        model.addAttribute("friendDetails", friendDetails);
        return "friends";
    }


    // CREATE

    // CREATE FRIENDSHIP INVITATION FOR CURRENTLY LOGGED IN MEMBER AND ANOTHER MEMBER
    @PostMapping("/friendship-invitation")
    public String sendFriendshipInvitation(@RequestParam("acceptingMemberId") Long acceptingMemberId) {
        Member invitingMember = memberService.getAuthenticatedMember();

        Member acceptingMember = memberService.findById(acceptingMemberId);

        friendshipService.createFriendshipInvitation(invitingMember, acceptingMember);

        // Send email to accepting member
        emailService.sendFriendshipInvitationEmail(acceptingMember, invitingMember);
        return "redirect:/mvc/members";
    }


    // UPDATE

    // ACCEPT FRIENDSHIP INVITATION
    @PostMapping("/accept-friendship-invitation")
    public String acceptFriendshipInvitation(@RequestParam("friendshipId") Long friendshipId) {
        friendshipService.acceptFriendshipInvitation(friendshipId);
        return "redirect:/mvc/friends";
    }

    // DELETE

    // DECLINE FRIENDSHIP INVITATION
    @PostMapping("/decline-friendship-invitation")
    public String declineFriendshipInvitation(@RequestParam("friendshipId") Long friendshipId) {
        friendshipService.declineFriendshipInvitation(friendshipId);
        return "redirect:/mvc/members";
    }


    // REMOVE FRIENDSHIP INVITATION
    @PostMapping("/remove-friendship-invitation")
    public String removeFriendshipInvitation(@RequestParam("friendshipId") Long friendshipId) {
        friendshipService.declineFriendshipInvitation(friendshipId);
        return "redirect:/mvc/members";
    }

}
