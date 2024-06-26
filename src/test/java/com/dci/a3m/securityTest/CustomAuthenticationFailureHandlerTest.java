package com.dci.a3m.securityTest;



import com.dci.a3m.security.CustomAuthenticationFailureHandler;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.core.AuthenticationException;
import java.io.IOException;
import static org.mockito.Mockito.*;

class CustomAuthenticationFailureHandlerTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private AuthenticationException exception;

    private CustomAuthenticationFailureHandler handler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        handler = new CustomAuthenticationFailureHandler();
    }

    @Test
    void testOnAuthenticationFailure_BlockAccount() throws IOException, ServletException {
        AuthenticationException accountStatusException = mock(AccountStatusException.class);

        handler.onAuthenticationFailure(request, response, accountStatusException);

        verify(response, times(1)).sendRedirect("/login-form?blocked");
    }

    @Test
    void testOnAuthenticationFailure_OtherError() throws IOException, ServletException {
        handler.onAuthenticationFailure(request, response, exception);

        verify(response, times(1)).sendRedirect("/login-form?error");
    }
}
