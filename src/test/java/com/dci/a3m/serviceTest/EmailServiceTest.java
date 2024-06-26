package com.dci.a3m.serviceTest;

import com.dci.a3m.entity.Admin;
import com.dci.a3m.entity.Member;
import com.dci.a3m.entity.Token;
import com.dci.a3m.entity.User;
import com.dci.a3m.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class EmailServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private TokenService tokenService;

    @Mock
    private UserService userService;

    @Mock
    private MemberService memberService;

    @Mock
    private AdminService adminService;

    @InjectMocks
    private EmailService emailService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void sendEmail() {
        String to = "ThomasLake@example.com";
        String subject = "Test Subject";
        String text = "Test Text";

        emailService.sendEmail(to, subject, text);

        ArgumentCaptor<SimpleMailMessage> messageCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender, times(1)).send(messageCaptor.capture());
        SimpleMailMessage sentMessage = messageCaptor.getValue();

        assertEquals("coders-a3m@mailfence.com", sentMessage.getFrom());
        assertEquals(to, sentMessage.getTo()[0]);
        assertEquals(subject, sentMessage.getSubject());
        assertEquals(text, sentMessage.getText());
    }

    @Test
    void sendResetPasswordEmail_Member() {
        Member member = new Member();
        User user = new User();
        user.setEmail("ThomasLake@example.com");
        member.setUser(user);
        Token existingToken = new Token();
        existingToken.setToken(UUID.randomUUID().toString());
        existingToken.setUser(user);

        when(tokenService.findByUser(user)).thenReturn(existingToken);

        emailService.sendResetPasswordEmail(member);

        verify(tokenService, times(1)).delete(existingToken);
        ArgumentCaptor<Token> tokenCaptor = ArgumentCaptor.forClass(Token.class);
        verify(tokenService, times(1)).save(tokenCaptor.capture());
        Token newToken = tokenCaptor.getValue();

        ArgumentCaptor<SimpleMailMessage> messageCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender, times(1)).send(messageCaptor.capture());
        SimpleMailMessage sentMessage = messageCaptor.getValue();

        assertEquals("coders-a3m@mailfence.com", sentMessage.getFrom());
        assertEquals("ThomasLake@example.com", sentMessage.getTo()[0]);
        assertEquals("Password Reset Request", sentMessage.getSubject());
        assertEquals("To reset your password, please click the link below:\n" +
                "http://coder-025.eu-central-1.elasticbeanstalk.com/reset-password?token=" + newToken.getToken() + "\nThis token will expire in 24 hours.", sentMessage.getText());
    }

    @Test
    void sendResetPasswordEmail_Admin() {
        Admin admin = new Admin();
        User user = new User();
        user.setEmail("ThomasLake@example.com");
        admin.setUser(user);
        Token existingToken = new Token();
        existingToken.setToken(UUID.randomUUID().toString());
        existingToken.setUser(user);

        when(tokenService.findByUser(user)).thenReturn(existingToken);

        emailService.sendResetPasswordEmail(admin);

        verify(tokenService, times(1)).delete(existingToken);
        ArgumentCaptor<Token> tokenCaptor = ArgumentCaptor.forClass(Token.class);
        verify(tokenService, times(1)).save(tokenCaptor.capture());
        Token newToken = tokenCaptor.getValue();

        ArgumentCaptor<SimpleMailMessage> messageCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender, times(1)).send(messageCaptor.capture());
        SimpleMailMessage sentMessage = messageCaptor.getValue();

        assertEquals("coders-a3m@mailfence.com", sentMessage.getFrom());
        assertEquals("ThomasLake@example.com", sentMessage.getTo()[0]);
        assertEquals("Password Reset Request", sentMessage.getSubject());
        assertEquals("To reset your password, please click the link below:\n" +
                "http://coder-025.eu-central-1.elasticbeanstalk.com/reset-password?token=" + newToken.getToken() + "\nThis token will expire in 24 hours.", sentMessage.getText());
    }
}
