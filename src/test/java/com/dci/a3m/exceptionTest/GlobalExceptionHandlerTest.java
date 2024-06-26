package com.dci.a3m.exceptionTest;

import com.dci.a3m.exception.GlobalExceptionHandler;
import com.dci.a3m.exception.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GlobalExceptionHandlerTest {

    @Mock
    private Model model;

    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testHandleUserNotFoundException() {
        UserNotFoundException ex = new UserNotFoundException("User not found");
        String viewName = globalExceptionHandler.handleUserNotFoundException(ex, model);

        assertEquals("member-error", viewName);
        verify(model).addAttribute("errorMessage", "User not found");
    }
}