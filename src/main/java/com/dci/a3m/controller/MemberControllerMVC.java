package com.dci.a3m.controller;


import com.dci.a3m.entity.*;
import com.dci.a3m.service.*;
import com.dci.a3m.util.PasswordValidator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/mvc")
public class MemberControllerMVC {

    private final WeatherService weatherService;
    MemberService memberService;
    UserService userService;
    PasswordEncoder passwordEncoder;
    AuthenticationManager authenticationManager;
    FriendshipService friendshipService;
    LikeService likeService;
    PostService postService;
    EmailService emailService;

    @Autowired
    public MemberControllerMVC(MemberService memberService, UserService userService, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, FriendshipService friendshipService, LikeService likeService, PostService postService, EmailService emailService, WeatherService weatherService) {
        this.memberService = memberService;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.friendshipService = friendshipService;
        this.likeService = likeService;
        this.postService = postService;
        this.emailService = emailService;
        this.weatherService = weatherService;
    }

    // CRUD OPERATIONS

    // READ ALL
    @GetMapping("/members")
    public String findAll(Model model) {
        Member authenticatedMember = memberService.getAuthenticatedMember();
        List<Member> members = memberService.findAll().stream()
                .filter(member -> !member.getId().equals(authenticatedMember.getId()))
                .collect(Collectors.toList());

        List<FriendshipInvitation> friends = friendshipService.findFriendsAccepted(authenticatedMember);
        List<Long> friendIds = friends.stream()
                .map(friend -> friend.getInvitingMember().getId().equals(authenticatedMember.getId()) ? friend.getAcceptingMember().getId() : friend.getInvitingMember().getId())
                .collect(Collectors.toList());

        Map<Long, Long> friendshipIdMap = friends.stream()
                .collect(Collectors.toMap(
                        friend -> friend.getInvitingMember().getId().equals(authenticatedMember.getId()) ? friend.getAcceptingMember().getId() : friend.getInvitingMember().getId(),
                        FriendshipInvitation::getId
                ));

        List<FriendshipInvitation> pendingReceivedInvitations = friendshipService.findByAcceptingMemberAndNotAccepted(authenticatedMember);
        List<Long> pendingReceivedIds = pendingReceivedInvitations.stream()
                .map(friend -> friend.getInvitingMember().getId())
                .collect(Collectors.toList());

        List<FriendshipInvitation> pendingSentInvitations = friendshipService.findByInvitingMemberAndNotAccepted(authenticatedMember);
        List<Long> pendingSentIds = pendingSentInvitations.stream()
                .map(friend -> friend.getAcceptingMember().getId())
                .collect(Collectors.toList());

        List<FriendshipInvitation> invitations = friendshipService.findByAcceptingMemberAndNotAccepted(authenticatedMember);
        model.addAttribute("invitations", invitations);
        model.addAttribute("members", members);
        model.addAttribute("friendIds", friendIds);
        model.addAttribute("pendingSentIds", pendingSentIds);
        model.addAttribute("pendingReceivedIds", pendingReceivedIds);
        model.addAttribute("friendshipIdMap", friendshipIdMap); // Add this line


        return "members";
    }

    // READ BY ID
    @GetMapping("/members/")
    public String getMemberById(@RequestParam("memberId") Long id, Model model) {


        Member authenticatedMember = memberService.findById(id);


        if (authenticatedMember == null) {
            model.addAttribute("error", "Member not found.");
            return "member-error";
        }

        // Prepare Posts attributes for Thymeleaf
        List<Post> posts = authenticatedMember.getPosts();

        // desc order
        posts.sort((p1, p2) -> p2.getCreatedAt().compareTo(p1.getCreatedAt()));

        Map<Long, Boolean> likedYourPosts = new HashMap<>();
        for (Post post : posts) {
            boolean liked = likeService.hasMemberLikedPost(authenticatedMember, post);
            likedYourPosts.put(post.getId(), liked);
        }

        model.addAttribute("posts", posts);
        model.addAttribute("member", authenticatedMember);
        model.addAttribute("authenticatedMember", authenticatedMember);
        model.addAttribute("likedYourPosts", likedYourPosts);


        // Prepare Friends attributes for Thymeleaf
        List<FriendshipInvitation> friendsAcceptedAndNotAccepted = friendshipService.findFriendsAccepted(authenticatedMember);
        List<FriendshipInvitation> friends = friendsAcceptedAndNotAccepted.stream().filter(friend -> friend.isAccepted()).collect(Collectors.toList());

        // desc order friends by friendshipId
        friends.sort((f1, f2) -> f2.getId().compareTo(f1.getId()));

        List<Long> friendIds = friends.stream()
                .map(friend -> friend.getInvitingMember().getId().equals(authenticatedMember.getId()) ? friend.getAcceptingMember().getId() : friend.getInvitingMember().getId())
                .collect(Collectors.toList());
        List<Map<String, Object>> friendDetails = friends.stream().map(friend -> {
            Map<String, Object> details = new HashMap<>();




            details.put("friendshipId", friend.getId());

            if (friend.getInvitingMember().getId().equals(authenticatedMember.getId())) {
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

        // Friendship Invitations
        List<FriendshipInvitation> invitations = friendshipService.findByAcceptingMemberAndNotAccepted(authenticatedMember);
        model.addAttribute("invitations", invitations);

        // desc order invitations
        invitations.sort((i1, i2) -> i2.getId().compareTo(i1.getId()));


        // Friends Posts
        List<Post> friendPosts = postService.findAll().stream()
                .filter(post -> friendIds.contains(post.getMember().getId()))
                .collect(Collectors.toList());

        // desc order
        friendPosts.sort((p1, p2) -> p2.getCreatedAt().compareTo(p1.getCreatedAt()));

        Map<Long, Boolean> likedFriendsPosts = new HashMap<>();
        for (Post post : friendPosts) {
            boolean liked = likeService.hasMemberLikedPost(authenticatedMember, post);
            likedFriendsPosts.put(post.getId(), liked);
        }

        model.addAttribute("friends", friends);
        model.addAttribute("friendPosts", friendPosts);
        model.addAttribute("likedFriendsPosts", likedFriendsPosts);

        // Weather information
        String city = authenticatedMember.getCity();
        String weather = weatherService.getWeather(city);
        model.addAttribute("weather", weather);

        return "member-info";
    }

    // READ BY USERNAME
    // Controller method for searching by username
    @GetMapping("/members/search")
    public String searchByUsername(@RequestParam("username") String username, Model model, RedirectAttributes redirectAttributes) {
        try {
            Member authenticatedMember = memberService.getAuthenticatedMember();
            Member member = memberService.findByUsername(username);

            if (member == null) {
                model.addAttribute("member-error", "Member not found.");
                redirectAttributes.addFlashAttribute("error", "Member not found");
                return "redirect:/mvc/members";
            }

            // Add the search result to the model
            model.addAttribute("members", List.of(member));  // Only add the found member

            // Prepare the friend-related attributes
            List<FriendshipInvitation> friends = friendshipService.findFriendsAccepted(authenticatedMember);
            List<Long> friendIds = friends.stream()
                    .map(friend -> friend.getInvitingMember().getId().equals(authenticatedMember.getId()) ? friend.getAcceptingMember().getId() : friend.getInvitingMember().getId())
                    .collect(Collectors.toList());

            Map<Long, Long> friendshipIdMap = friends.stream()
                    .collect(Collectors.toMap(
                            friend -> friend.getInvitingMember().getId().equals(authenticatedMember.getId()) ? friend.getAcceptingMember().getId() : friend.getInvitingMember().getId(),
                            FriendshipInvitation::getId
                    ));

            List<FriendshipInvitation> pendingReceivedInvitations = friendshipService.findByAcceptingMemberAndNotAccepted(authenticatedMember);
            List<Long> pendingReceivedIds = pendingReceivedInvitations.stream()
                    .map(friend -> friend.getInvitingMember().getId())
                    .collect(Collectors.toList());

            List<FriendshipInvitation> pendingSentInvitations = friendshipService.findByInvitingMemberAndNotAccepted(authenticatedMember);
            List<Long> pendingSentIds = pendingSentInvitations.stream()
                    .map(friend -> friend.getAcceptingMember().getId())
                    .collect(Collectors.toList());

            List<FriendshipInvitation> invitations = friendshipService.findByAcceptingMemberAndNotAccepted(authenticatedMember);
            model.addAttribute("invitations", invitations);
            model.addAttribute("friendIds", friendIds);
            model.addAttribute("pendingSentIds", pendingSentIds);
            model.addAttribute("pendingReceivedIds", pendingReceivedIds);
            model.addAttribute("friendshipIdMap", friendshipIdMap);

            redirectAttributes.addFlashAttribute("success", "Member found");
            return "members";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Member not found");
            return "redirect:/mvc/members";
        }
    }


    // CREATE - SHOW FORM
    @GetMapping("/member-form")
    public String showMemberForm(Model model) {
        model.addAttribute("member", new Member());
        return "member-form";
    }

    // UPDATE - SHOW FORM
    @GetMapping("/member-form-update")
    public String showMemberFormUpdate(Model model, RedirectAttributes redirectAttributes) {
        Member authenticatedMember = memberService.getAuthenticatedMember();
        if (authenticatedMember == null) {

            redirectAttributes.addFlashAttribute("error", "Member not found.");
            return "redirect:/mvc/home";
        }
        User user = authenticatedMember.getUser();
        authenticatedMember.setUser(user);
        model.addAttribute("member", authenticatedMember);
        return "member-form";
    }

    // SAVE FORM
    @PostMapping("/member-form/create")
    public String saveMember(@Valid @ModelAttribute("member") Member member, BindingResult result, RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            return "member-form"; // Return to form if validation fails
        }

        try {
            // PasswordEncoder
            User tempUser = member.getUser();
            String email = tempUser.getEmail();
            String username = tempUser.getUsername();
            if(userService.findByEmail(email) != null || userService.findByUsername(username) != null) {
                redirectAttributes.addFlashAttribute("error", "Email or Username already exists.");
                return "redirect:/mvc/member-form";
            }
            tempUser.setMember(member);
            member.getUser().setPassword(passwordEncoder.encode(tempUser.getPassword()));
            tempUser.setEnabled(true);
            tempUser.setAuthority(new Authority(tempUser.getUsername(), member.getRole()));

            String initPhotoUrl = "https://cdn-icons-png.flaticon.com/512/11876/11876586.png";
            member.setProfilePicture(initPhotoUrl);
            member.setUser(tempUser);
            // Save Member
            memberService.save(member);
            userService.update(tempUser);

            // Send email
            emailService.sendEmail(member.getUser().getEmail(), "Welcome to CODERS by A3M", "Welcome to CODERS, " + member.getUser().getUsername() + "!");

            redirectAttributes.addFlashAttribute("success", "Member created successfully.");
            return "redirect:/mvc/members/?memberId=" + member.getId();
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error creating member.");
            return "redirect:/mvc/member-form";
        }
    }

    // UPDATE FORM
    @PostMapping("/member-form/update")
    public String updateMember(@Valid @ModelAttribute("member") Member member, BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "member-form"; // Return to form if validation fails
        }

        try {
            Member existingMember = memberService.findById(member.getId());

            if (existingMember == null) {
                redirectAttributes.addFlashAttribute("error", "Member not found.");
                return "redirect:/mvc/members/?memberId=" + member.getId();
            }

            // Update the user details
            User tempUser = existingMember.getUser();
            tempUser.setEmail(member.getUser().getEmail());
            tempUser.setUsername(member.getUser().getUsername());

            tempUser.setAuthority(new Authority(tempUser.getUsername(), member.getRole()));
            existingMember.setUser(tempUser);
            existingMember.setRole(member.getRole());

            existingMember.setFirstName(member.getFirstName());
            existingMember.setLastName(member.getLastName());
            existingMember.setProfilePicture(member.getProfilePicture());
            existingMember.setCity(member.getCity());
            existingMember.setCountry(member.getCountry());
            existingMember.setPostalCode(member.getPostalCode());
            existingMember.setPhone(member.getPhone());

            memberService.update(existingMember);
            userService.update(tempUser);

            redirectAttributes.addFlashAttribute("success", "Member updated successfully.");
            return "redirect:/mvc/members/?memberId=" + member.getId();
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error updating member.");
            return "redirect:/mvc/member-form-update?memberId=" + member.getId();
        }
    }


    // DELETE
    @GetMapping("/member-delete")
    public String deleteMember(@RequestParam("memberId") Long id) {
        memberService.deleteById(id);
        return "redirect:/login-form?logout";
    }

    //CHANGE PASSWORD
    @GetMapping("/member-change-password")
    public String changePassword(@RequestParam("memberId") Long id, Model model) {
        Member member = memberService.findById(id);
        if (member == null) {
            model.addAttribute("error", "Member not found.");
            return "member-error";
        }
        model.addAttribute("member", member);
        return "member-change-password";
    }

    @PostMapping("/member-change-password")
    public String changePassword(@RequestParam("currentPassword") String currentPassword,
                                 @RequestParam("newPassword") String newPassword,
                                 @RequestParam("confirmNewPassword") String confirmNewPassword,
                                 @RequestParam("memberId") Long id, RedirectAttributes redirectAttributes) {


        Member member = memberService.findById(id);
        if (member == null) {
            return "member-error";
        }
        User user = member.getUser();
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            redirectAttributes.addFlashAttribute("error", "Current Password is incorrect.");
            return "redirect:/mvc/member-change-password?memberId=" + id;
        }
        if (!newPassword.equals(confirmNewPassword)) {
            redirectAttributes.addFlashAttribute("error", "New Passwords do no match.");
            return "redirect:/mvc/member-change-password?memberId=" + id;
        }

        //Validate password
        if(!PasswordValidator.isValid(newPassword)){
            redirectAttributes.addFlashAttribute("error", "Password must contain at least 8 characters, one uppercase letter, one lowercase letter, one number and one special character.");
            return "redirect:/mvc/member-change-password?memberId=" + id;
        }

        else {
            user.setPassword(passwordEncoder.encode(newPassword));
            memberService.update(member);
            userService.update(user);
            redirectAttributes.addFlashAttribute("success", "Password changed successfully.");
            return "redirect:/login-success";
        }
    }



}
