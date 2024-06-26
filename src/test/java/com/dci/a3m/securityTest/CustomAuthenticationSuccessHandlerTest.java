package com.dci.a3m.securityTest;

import com.dci.a3m.entity.User;
import com.dci.a3m.security.CustomAuthenticationSuccessHandler;
import com.dci.a3m.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class CustomAuthenticationSuccessHandlerTest {

    @Mock
    private UserService userService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private Authentication authentication;

    @Mock
    private UserDetails userDetails;

    @Mock
    private HttpSession session;

    private CustomAuthenticationSuccessHandler handler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        handler = new CustomAuthenticationSuccessHandler(userService);
    }

    @Test
    void testOnAuthenticationSuccess_UserEnabled() throws IOException, ServletException {
        User user = new User();
        user.setEnabled(true);

        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn("AliceRiver");
        when(userService.findByUsername(anyString())).thenReturn(user);

        handler.onAuthenticationSuccess(request, response, authentication);

        verify(response).sendRedirect("/login-success");
    }

    @Test
    void testOnAuthenticationSuccess_UserDisabled() throws IOException, ServletException {
        User user = new User();
        user.setEnabled(false);

        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn("AliceRiver");
        when(userService.findByUsername(anyString())).thenReturn(user);
        when(request.getSession()).thenReturn(session);

        handler.onAuthenticationSuccess(request, response, authentication);

        verify(session).invalidate();
        verify(response).sendRedirect("/login-form?blocked=true");
    }
}
