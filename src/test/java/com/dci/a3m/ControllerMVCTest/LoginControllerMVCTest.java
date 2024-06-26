package com.dci.a3m.ControllerMVCTest;

import com.dci.a3m.controller.LoginControllerMVC;
import com.dci.a3m.entity.Admin;
import com.dci.a3m.entity.Member;
import com.dci.a3m.service.AdminService;
import com.dci.a3m.service.EmailService;
import com.dci.a3m.service.MemberService;
import com.dci.a3m.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LoginControllerMVC.class)
public class LoginControllerMVCTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MemberService memberService;

    @MockBean
    private AdminService adminService;

    @MockBean
    private UserService userService;

    @MockBean
    private EmailService emailService;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void setup(WebApplicationContext context) {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    public void testLogin() throws Exception {
        mockMvc.perform(get("/login-form"))
                .andExpect(status().isOk())
                .andExpect(view().name("login-form"));
    }

    @Test
    public void testLoginSuccessMember() throws Exception {
        Member member = new Member();
        member.setId(1L);
        member.setFirstName("John");
        member.setLastName("Doe");

        Mockito.when(memberService.getAuthenticatedMember()).thenReturn(member);
        Mockito.when(adminService.getAuthenticatedAdmin()).thenReturn(null);

        mockMvc.perform(get("/login-success"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/mvc/posts-of-friends"));
    }

    @Test
    public void testLoginSuccessAdmin() throws Exception {
        Admin admin = new Admin();
        Mockito.when(adminService.getAuthenticatedAdmin()).thenReturn(admin);

        mockMvc.perform(get("/login-success"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin-dashboard/admin-dashboard"));
    }

    @Test
    public void testForgotPasswordPage() throws Exception {
        mockMvc.perform(get("/forgot-password"))
                .andExpect(status().isOk())
                .andExpect(view().name("forgot-password"));
    }

    // Add more tests for other endpoints...
}

