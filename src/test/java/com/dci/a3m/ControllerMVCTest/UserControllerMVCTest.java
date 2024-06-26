package com.dci.a3m.ControllerMVCTest;



import com.dci.a3m.controller.UserControllerMVC;
import com.dci.a3m.entity.Authority;
import com.dci.a3m.entity.User;
import com.dci.a3m.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class UserControllerMVCTest {

    @Mock
    private UserService userService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private Model model;

    @InjectMocks
    private UserControllerMVC userControllerMVC;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userControllerMVC).build();
    }

    @Test
    void findAll() throws Exception {
        List<User> users = new ArrayList<>();
        users.add(new User());
        when(userService.findAll()).thenReturn(users);

        mockMvc.perform(get("/mvc/users"))
                .andExpect(status().isOk())
                .andExpect(view().name("restricted/users"))
                .andExpect(model().attributeExists("users"));

        verify(userService, times(1)).findAll();
    }

    @Test
    void getUserById() throws Exception {
        User user = new User();
        user.setId(1L);
        when(userService.findById(1L)).thenReturn(user);

        mockMvc.perform(get("/mvc/users/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("restricted/user-info"))
                .andExpect(model().attributeExists("user"));

        verify(userService, times(1)).findById(1L);
    }

    @Test
    void getUserById_NotFound() throws Exception {
        when(userService.findById(1L)).thenReturn(null);

        mockMvc.perform(get("/mvc/users/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("restricted/user-error"))
                .andExpect(model().attributeExists("error"));

        verify(userService, times(1)).findById(1L);
    }

    @Test
    void registerUser() throws Exception {
        mockMvc.perform(get("/mvc/user-form"))
                .andExpect(status().isOk())
                .andExpect(view().name("restricted/user-form"))
                .andExpect(model().attributeExists("user"));
    }

    @Test
    void saveUser() throws Exception {
        User user = new User();
        user.setUsername("testUser");
        user.setPassword("testPassword");

        when(passwordEncoder.encode("testPassword")).thenReturn("encodedPassword");

        mockMvc.perform(post("/mvc/user-form")
                        .flashAttr("user", user))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/mvc/users"));

        verify(passwordEncoder, times(1)).encode("testPassword");
        verify(userService, times(1)).save(any(User.class));
    }

    @Test
    void deleteUser() throws Exception {
        mockMvc.perform(get("/mvc/deleteUser")
                        .param("userId", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/mvc/users"));

        verify(userService, times(1)).deleteById(1L);
    }
}
