package com.dci.a3m.ControllerMVCTest;


import com.dci.a3m.controller.EmailController;
import com.dci.a3m.service.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
public class EmailControllerTest {

    @Mock
    private EmailService emailService;

    @InjectMocks
    private EmailController emailController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSendEmail() {
        String to = "test@example.com";
        String subject = "Test Subject";
        String text = "Test email content";

        String response = emailController.sendEmail(to, subject, text);
        assertEquals("Email sent successfully", response);

        verify(emailService).sendEmail(to, subject, text);
    }
}

