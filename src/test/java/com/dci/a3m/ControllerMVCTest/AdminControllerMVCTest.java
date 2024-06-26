package com.dci.a3m.ControllerMVCTest;


import com.dci.a3m.controller.AdminControllerMVC;
import com.dci.a3m.entity.*;
import com.dci.a3m.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class AdminControllerMVCTest {

    @Mock
    private AdminService adminService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserService userService;

    @Mock
    private MemberService memberService;

    @Mock
    private PostService postService;

    @Mock
    private CommentService commentService;

    @InjectMocks
    private AdminControllerMVC adminControllerMVC;

    @Mock
    private Model model;

    @Mock
    private RedirectAttributes redirectAttributes;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAdminDashboard() {
        String viewName = adminControllerMVC.adminDashboard();
        assertEquals("restricted/admin-dashboard", viewName);
    }

    @Test
    public void testMembersList() {
        List<Member> members = new ArrayList<>();
        members.add(new Member());
        when(memberService.findAll()).thenReturn(members);

        String viewName = adminControllerMVC.membersList(model);
        assertEquals("restricted/members-list", viewName);
        verify(model).addAttribute("members", members);
    }

    @Test
    public void testSearchUsernameMemberFound() {
        Member member = new Member();
        when(memberService.findByUsername("AliceRiver")).thenReturn(member);

        String viewName = adminControllerMVC.searchUsername("AliceRiver", model, redirectAttributes);
        assertEquals("restricted/members-list", viewName);
        verify(model).addAttribute("members", List.of(member));
        verify(model).addAttribute("success", "Member founded");
    }

    @Test
    public void testSearchUsernameMemberNotFound() {
        when(memberService.findByUsername("AliceRiver")).thenReturn(null);

        String viewName = adminControllerMVC.searchUsername("AliceRiver", model, redirectAttributes);
        assertEquals("redirect:/admin-dashboard/members-list", viewName);
        verify(redirectAttributes).addFlashAttribute("error", "Member not found.");
    }

    @Test
    public void testBlockMember() {
        Member member = new Member();
        User user = new User();
        member.setUser(user);
        when(memberService.findById(1L)).thenReturn(member);

        String viewName = adminControllerMVC.blockMember(1L, false, redirectAttributes);
        assertEquals("redirect:/admin-dashboard/members-list", viewName);
        verify(userService).update(user);
        verify(redirectAttributes).addFlashAttribute("success", "Member has been blocked.");
    }

    @Test
    public void testUnblockMember() {
        Member member = new Member();
        User user = new User();
        member.setUser(user);
        when(memberService.findById(1L)).thenReturn(member);

        String viewName = adminControllerMVC.unblockMember(1L, true, redirectAttributes);
        assertEquals("redirect:/admin-dashboard/members-list", viewName);
        verify(userService).update(user);
        verify(redirectAttributes).addFlashAttribute("success", "Member has been unblocked.");
    }

    @Test
    public void testDeleteMember() {
        Member member = new Member();
        when(memberService.findById(1L)).thenReturn(member);

        String viewName = adminControllerMVC.deleteMember(1L, redirectAttributes);
        assertEquals("redirect:/admin-dashboard/members-list", viewName);
        verify(memberService).deleteById(1L);
        verify(redirectAttributes).addFlashAttribute("success", "Member has been deleted.");
    }
}

