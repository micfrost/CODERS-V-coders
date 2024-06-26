package com.dci.a3m.securityTest;

import com.dci.a3m.security.CustomAuthenticationFailureHandler;
import com.dci.a3m.security.WebSecurityConfig;
import com.dci.a3m.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class WebSecurityConfigTest {

    @Mock
    private UserService userService;

    @Mock
    private DataSource dataSource;

    @Mock
    private CustomAuthenticationFailureHandler customAuthenticationFailureHandler;

    @Mock
    private AuthenticationConfiguration authenticationConfiguration;

    @InjectMocks
    private WebSecurityConfig webSecurityConfig;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        webSecurityConfig = new WebSecurityConfig(userService);
    }

    @Test
    void testPasswordEncoder() {
        PasswordEncoder passwordEncoder = webSecurityConfig.passwordEncoder();
        assertNotNull(passwordEncoder);
    }

    @Test
    void testUserDetailsManager() {
        UserDetailsManager userDetailsManager = webSecurityConfig.userDetailsManager(dataSource);
        assertNotNull(userDetailsManager);
    }

    @Test
    void testAuthenticationManager() throws Exception {
        AuthenticationManager authenticationManager = mock(AuthenticationManager.class);

        when(authenticationConfiguration.getAuthenticationManager()).thenReturn(authenticationManager);

        AuthenticationManager result = webSecurityConfig.authenticationManager(authenticationConfiguration);
        assertNotNull(result);
    }

//    @Test
//    void testSecurityFilterChain() throws Exception {
//        HttpSecurity httpSecurity = mock(HttpSecurity.class);
//
//        SecurityFilterChain securityFilterChain = webSecurityConfig.securityFilterChain(httpSecurity, customAuthenticationFailureHandler);
//        assertNotNull(securityFilterChain);
//
//    }
}
