package com.dci.a3m.ControllerMVCTest;

import com.dci.a3m.controller.FriendshipControllerMVC;
import com.dci.a3m.entity.FriendshipInvitation;
import com.dci.a3m.entity.Member;
import com.dci.a3m.entity.User;
import com.dci.a3m.service.EmailService;
import com.dci.a3m.service.FriendshipService;
import com.dci.a3m.service.MemberService;
import com.dci.a3m.service.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import java.util.ArrayList;
import java.util.List;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class FriendshipControllerMVCTest {

    @Mock
    private FriendshipService friendshipService;

    @Mock
    private MemberService memberService;

    @Mock
    private PostService postService;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private FriendshipControllerMVC friendshipControllerMVC;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/views/");
        viewResolver.setSuffix(".jsp");

        mockMvc = MockMvcBuilders.standaloneSetup(friendshipControllerMVC)
                .setViewResolvers(viewResolver)
                .build();
    }

    @Test
    void showFriendshipInvitations() throws Exception {
        Member member = new Member();
        List<FriendshipInvitation> invitations = new ArrayList<>();
        invitations.add(new FriendshipInvitation());

        when(memberService.getAuthenticatedMember()).thenReturn(member);
        when(friendshipService.findByAcceptingMemberAndNotAccepted(member)).thenReturn(invitations);

        mockMvc.perform(get("/mvc/friendship-invitations"))
                .andExpect(status().isOk())
                .andExpect(view().name("friendship-invitations"))
                .andExpect(model().attributeExists("invitations"));

        verify(friendshipService, times(1)).findByAcceptingMemberAndNotAccepted(member);
    }

    @Test
    void showFriends() throws Exception {
        Member member = new Member();
        member.setId(1L); // Set member ID
        List<FriendshipInvitation> friendsAcceptedAndNotAccepted = new ArrayList<>();
        FriendshipInvitation invitation = new FriendshipInvitation();
        invitation.setAccepted(true);
        invitation.setInvitingMember(member); // Ensure inviting member is set

        Member acceptingMember = new Member();
        acceptingMember.setId(2L);
        acceptingMember.setFirstName("Emma");
        acceptingMember.setLastName("Ocean");

        User user = new User();
        user.setUsername("EmmaOcean"); // Use setter method to set username
        acceptingMember.setUser(user);

        invitation.setAcceptingMember(acceptingMember);

        friendsAcceptedAndNotAccepted.add(invitation);

        when(memberService.getAuthenticatedMember()).thenReturn(member);
        when(friendshipService.findFriendsAccepted(member)).thenReturn(friendsAcceptedAndNotAccepted);

        mockMvc.perform(get("/mvc/friends"))
                .andExpect(status().isOk())
                .andExpect(view().name("friends"))
                .andExpect(model().attributeExists("friendDetails"));

        verify(friendshipService, times(1)).findFriendsAccepted(member);
    }

    @Test
    void sendFriendshipInvitation() throws Exception {
        Member invitingMember = new Member();
        Member acceptingMember = new Member();
        acceptingMember.setId(1L);

        when(memberService.getAuthenticatedMember()).thenReturn(invitingMember);
        when(memberService.findById(1L)).thenReturn(acceptingMember);

        mockMvc.perform(post("/mvc/friendship-invitation")
                        .param("acceptingMemberId", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/mvc/members"));

        verify(friendshipService, times(1)).createFriendshipInvitation(invitingMember, acceptingMember);
        verify(emailService, times(1)).sendFriendshipInvitationEmail(acceptingMember, invitingMember);
    }

    @Test
    void acceptFriendshipInvitation() throws Exception {
        mockMvc.perform(post("/mvc/accept-friendship-invitation")
                        .param("friendshipId", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/mvc/friends"));

        verify(friendshipService, times(1)).acceptFriendshipInvitation(1L);
    }

    @Test
    void declineFriendshipInvitation() throws Exception {
        mockMvc.perform(post("/mvc/decline-friendship-invitation")
                        .param("friendshipId", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/mvc/members"));

        verify(friendshipService, times(1)).declineFriendshipInvitation(1L);
    }

    @Test
    void removeFriendshipInvitation() throws Exception {
        mockMvc.perform(post("/mvc/remove-friendship-invitation")
                        .param("friendshipId", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/mvc/members"));

        verify(friendshipService, times(1)).declineFriendshipInvitation(1L);
    }
}
