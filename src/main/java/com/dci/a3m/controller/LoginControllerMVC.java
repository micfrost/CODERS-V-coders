package com.dci.a3m.controller;


import com.dci.a3m.entity.Admin;
import com.dci.a3m.entity.Member;
import com.dci.a3m.service.AdminService;
import com.dci.a3m.service.EmailService;
import com.dci.a3m.service.MemberService;
import com.dci.a3m.service.UserService;
import com.dci.a3m.util.PasswordValidator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

@Controller
public class LoginControllerMVC {
    private final MemberService memberService;
    private final AdminService adminService;
    private final UserService userService;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public LoginControllerMVC(MemberService memberService, AdminService adminService, UserService userService, EmailService emailService, PasswordEncoder passwordEncoder) {
        this.memberService = memberService;
        this.adminService = adminService;
        this.userService = userService;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
    }


    @GetMapping("/login-form")
    public String login() {
        return "login-form";
    }


    @GetMapping("/login-success")
    public String loginSuccess() {
        Member member = memberService.getAuthenticatedMember();
        Admin admin = adminService.getAuthenticatedAdmin();

            if (admin != null) {
                return "redirect:/admin-dashboard/admin-dashboard";
            }
        // If the member is new, redirect to the profile page to complete the registration
        if (member.getFirstName() == null || member.getLastName() == null) {
            return "redirect:/mvc/members/?memberId=" + member.getId();
        }
        return "redirect:/mvc/posts-of-friends";
    }


    @GetMapping("/logged-coder-or-admin")
    public String loggedCoderOrAdmin() {
        Member member = memberService.getAuthenticatedMember();
        Admin admin = adminService.getAuthenticatedAdmin();

        if (admin != null) {
            return "redirect:/admin-dashboard/admin-dashboard";
        }

        return "redirect:/mvc/members/?memberId=" + member.getId();
    }

    @GetMapping("/forgot-password")
    public String forgotPassword() {
        return "forgot-password";
    }

    @PostMapping("/forgot-password")
    public String forgotPassword(@RequestParam String email, @RequestParam String username, Model model, RedirectAttributes redirectAttributes) {
        Member member = memberService.findByEmail(email);
        Admin admin = adminService.findByEmail(email);

        if(member != null && (username.equals(member.getUser().getUsername()))){
            emailService.sendResetPasswordEmail(member);
            redirectAttributes.addFlashAttribute("success", "An email has been sent to " + email + " with instructions to reset your password.");
        } else if (admin != null && (username.equals(admin.getUser().getUsername()))){
            emailService.sendResetPasswordEmail(admin);
            model.addAttribute("success", "An email has been sent to " + email + " with instructions to reset your password.");
        }  else {
            model.addAttribute("error", "No account found for " + email);
            model.addAttribute("error", "The email and username do not match.");
            return "forgot-password";

        }

        return "redirect:/login-form";
    }

    @GetMapping("/reset-password")
    public String showResetPasswordForm(@RequestParam("token") String token, Model model, RedirectAttributes redirectAttributes){
        Member member = memberService.findByToken(token);
        if (member == null) {
            redirectAttributes.addFlashAttribute("error", "Invalid token.");
            return "redirect:/forgot-password";
        }
        model.addAttribute("token", token);
        model.addAttribute("member", member);
        return "reset-password";
    }

    @PostMapping("/reset-password")
    public String resetPassword(@RequestParam("token") String token,
                                @RequestParam("newPassword") String newPassword,
                                @RequestParam("confirmPassword") String confirmPassword,
                                Model model,
                                RedirectAttributes redirectAttributes ) {

        Member member = memberService.findByToken(token);
        if (member == null) {
            redirectAttributes.addFlashAttribute("error", "Invalid token.");
            return "redirect:/mvc/forgot-password";
        }

        // Check if token is expired
        LocalDateTime expiryDate = member.getUser().getToken().getExpirationDate();
        if (LocalDateTime.now().isAfter(expiryDate)) {
            redirectAttributes.addFlashAttribute("error", "Token has expired.");
            return "redirect:/mvc/forgot-password";
        }

        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("error", "Passwords do not match.");
            model.addAttribute("token", token);
            return "reset-password";
        }

        // Validate password
        if (!PasswordValidator.isValid(newPassword)) {
            model.addAttribute("error", "Password must be at least 8 characters long and contain at least one uppercase letter, one lowercase letter, one digit, and one special character.");
            model.addAttribute("token", token);
            return "reset-password";
        }

            member.getUser().setPassword(passwordEncoder.encode(newPassword));
            memberService.update(member);
            userService.update(member.getUser());

            redirectAttributes.addFlashAttribute("success", "Your password has been reset successfully.");
            return "redirect:/login-form";

    }


    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }

        return "redirect:/login-form?logout";

    }

}


