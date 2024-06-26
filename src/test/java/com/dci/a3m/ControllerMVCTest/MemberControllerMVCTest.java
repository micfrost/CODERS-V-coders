package com.dci.a3m.ControllerMVCTest;

import com.dci.a3m.controller.MemberControllerMVC;
import com.dci.a3m.entity.*;
import com.dci.a3m.service.*;
import com.dci.a3m.util.PasswordValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.MockedStatic;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class MemberControllerMVCTest {

    @Mock
    private MemberService memberService;

    @Mock
    private UserService userService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private FriendshipService friendshipService;

    @Mock
    private LikeService likeService;

    @Mock
    private PostService postService;

    @Mock
    private EmailService emailService;

    @Mock
    private WeatherService weatherService;

    @InjectMocks
    private MemberControllerMVC memberControllerMVC;

    @Mock
    private Model model;

    @Mock
    private BindingResult bindingResult;

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

        Member otherMember = new Member();
        otherMember.setId(2L);

        List<Member> members = new ArrayList<>();
        members.add(authenticatedMember);
        members.add(otherMember);

        when(memberService.getAuthenticatedMember()).thenReturn(authenticatedMember);
        when(memberService.findAll()).thenReturn(members);
        when(friendshipService.findFriendsAccepted(authenticatedMember)).thenReturn(Collections.emptyList());
        when(friendshipService.findByAcceptingMemberAndNotAccepted(authenticatedMember)).thenReturn(Collections.emptyList());
        when(friendshipService.findByInvitingMemberAndNotAccepted(authenticatedMember)).thenReturn(Collections.emptyList());

        String viewName = memberControllerMVC.findAll(model);

        assertEquals("members", viewName);
        verify(model).addAttribute("members", Collections.singletonList(otherMember));
    }

    @Test
    public void testGetMemberById() {
        Member member = new Member();
        member.setId(1L);
        member.setPosts(new ArrayList<>());

        when(memberService.findById(1L)).thenReturn(member);

        String viewName = memberControllerMVC.getMemberById(1L, model);

        assertEquals("member-info", viewName);
        verify(model).addAttribute("member", member);
    }

    @Test
    public void testSearchByUsername() {
        Member authenticatedMember = new Member();
        Member member = new Member();
        User user = new User();
        user.setUsername("EmmaOcean");
        member.setUser(user);

        when(memberService.getAuthenticatedMember()).thenReturn(authenticatedMember);
        when(memberService.findByUsername("EmmaOcean")).thenReturn(member);
        when(friendshipService.findFriendsAccepted(authenticatedMember)).thenReturn(Collections.emptyList());
        when(friendshipService.findByAcceptingMemberAndNotAccepted(authenticatedMember)).thenReturn(Collections.emptyList());
        when(friendshipService.findByInvitingMemberAndNotAccepted(authenticatedMember)).thenReturn(Collections.emptyList());

        String viewName = memberControllerMVC.searchByUsername("EmmaOcean", model, redirectAttributes);

        assertEquals("members", viewName);
        verify(model).addAttribute("members", Collections.singletonList(member));
    }

    @Test
    public void testShowMemberForm() {
        String viewName = memberControllerMVC.showMemberForm(model);
        assertEquals("member-form", viewName);
        verify(model).addAttribute(eq("member"), any(Member.class));
    }

    @Test
    public void testShowMemberFormUpdate() {
        Member member = new Member();
        User user = new User();
        member.setUser(user);

        when(memberService.getAuthenticatedMember()).thenReturn(member);

        String viewName = memberControllerMVC.showMemberFormUpdate(model, redirectAttributes);

        assertEquals("member-form", viewName);
        verify(model).addAttribute("member", member);
    }

    @Test
    public void testSaveMember() {
        Member member = new Member();
        User user = new User();
        user.setEmail("user@example.com");
        user.setUsername("user");
        member.setUser(user);

        when(bindingResult.hasErrors()).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userService.findByEmail(anyString())).thenReturn(null);
        when(userService.findByUsername(anyString())).thenReturn(null);

        String viewName = memberControllerMVC.saveMember(member, bindingResult, redirectAttributes);

        assertEquals("redirect:/mvc/members/?memberId=" + member.getId(), viewName);
        verify(memberService).save(member);
        verify(userService).update(user);
        verify(emailService).sendEmail(eq(user.getEmail()), anyString(), anyString());
    }

    @Test
    public void testUpdateMember() {
        Member member = new Member();
        User user = new User();
        member.setUser(user);

        Member existingMember = new Member();
        existingMember.setUser(new User());

        when(memberService.findById(member.getId())).thenReturn(existingMember);
        when(bindingResult.hasErrors()).thenReturn(false);

        String viewName = memberControllerMVC.updateMember(member, bindingResult, redirectAttributes);

        assertEquals("redirect:/mvc/members/?memberId=" + member.getId(), viewName);
        verify(memberService).update(existingMember);
        verify(userService).update(existingMember.getUser());
    }

    @Test
    public void testDeleteMember() {
        String viewName = memberControllerMVC.deleteMember(1L);
        assertEquals("redirect:/login-form?logout", viewName);
        verify(memberService).deleteById(1L);
    }

    @Test
    public void testChangePassword() {
        Member member = new Member();
        member.setId(1L);

        when(memberService.findById(1L)).thenReturn(member);

        String viewName = memberControllerMVC.changePassword(1L, model);

        assertEquals("member-change-password", viewName);
        verify(model).addAttribute("member", member);
    }

    @Test
    public void testChangePasswordPost() {
        Member member = new Member();
        User user = new User();
        user.setPassword("currentPassword");
        member.setUser(user);

        when(memberService.findById(1L)).thenReturn(member);
        when(passwordEncoder.matches("currentPassword", "currentPassword")).thenReturn(true);
        when(passwordEncoder.encode("newPassword")).thenReturn("encodedNewPassword");

        try (MockedStatic<PasswordValidator> mockedPasswordValidator = mockStatic(PasswordValidator.class)) {
            mockedPasswordValidator.when(() -> PasswordValidator.isValid("newPassword")).thenReturn(true);

            String viewName = memberControllerMVC.changePassword("currentPassword", "newPassword", "newPassword", 1L, redirectAttributes);

            assertEquals("redirect:/login-success", viewName);
            verify(memberService).update(member);
            verify(userService).update(member.getUser());
        }
    }
}
