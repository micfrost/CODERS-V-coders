package com.dci.a3m.security;

import com.dci.a3m.entity.Member;
import com.dci.a3m.entity.User;
import com.dci.a3m.service.AdminService;
import com.dci.a3m.service.MemberService;
import com.dci.a3m.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {


    private final UserService userService;


    @Autowired
    public CustomAuthenticationSuccessHandler(UserService userService) {
        this.userService = userService;

    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userService.findByUsername(userDetails.getUsername());

        if (!user.isEnabled()) {
            request.getSession().invalidate();
            response.sendRedirect("/login-form?blocked=true");
        }
        else {

            response.sendRedirect("/login-success");
        }

    }
}
