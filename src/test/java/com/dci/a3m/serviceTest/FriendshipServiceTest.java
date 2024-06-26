package com.dci.a3m.serviceTest;

import com.dci.a3m.entity.FriendshipInvitation;
import com.dci.a3m.entity.Member;
import com.dci.a3m.repository.FriendshipInvitationRepository;
import com.dci.a3m.service.FriendshipServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class FriendshipServiceTest {

    @Mock
    private FriendshipInvitationRepository friendshipInvitationRepository;

    @InjectMocks
    private FriendshipServiceImpl friendshipService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindByAcceptingMemberAndNotAccepted() {
        Member member = new Member();
        FriendshipInvitation invitation = new FriendshipInvitation();
        List<FriendshipInvitation> invitations = Collections.singletonList(invitation);

        when(friendshipInvitationRepository.findByAcceptingMemberAndAccepted(member, false)).thenReturn(invitations);

        List<FriendshipInvitation> result = friendshipService.findByAcceptingMemberAndNotAccepted(member);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testFindByInvitingMemberAndNotAccepted() {
        Member member = new Member();
        FriendshipInvitation invitation = new FriendshipInvitation();
        List<FriendshipInvitation> invitations = Collections.singletonList(invitation);

        when(friendshipInvitationRepository.findByInvitingMemberAndAccepted(member, false)).thenReturn(invitations);

        List<FriendshipInvitation> result = friendshipService.findByInvitingMemberAndNotAccepted(member);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testFindFriendsAccepted() {
        Member member = new Member();
        FriendshipInvitation invitation = new FriendshipInvitation();
        List<FriendshipInvitation> invitations = Collections.singletonList(invitation);

        when(friendshipInvitationRepository.findByInvitingMemberOrAcceptingMemberAndAccepted(member, member, true)).thenReturn(invitations);

        List<FriendshipInvitation> result = friendshipService.findFriendsAccepted(member);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testFindById() {
        FriendshipInvitation invitation = new FriendshipInvitation();
        when(friendshipInvitationRepository.findById(anyLong())).thenReturn(Optional.of(invitation));

        FriendshipInvitation result = friendshipService.findById(1L);

        assertNotNull(result);
        assertEquals(invitation, result);
    }

    @Test
    void testFindAll() {
        FriendshipInvitation invitation = new FriendshipInvitation();
        List<FriendshipInvitation> invitations = Collections.singletonList(invitation);

        when(friendshipInvitationRepository.findAll()).thenReturn(invitations);

        List<FriendshipInvitation> result = friendshipService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testCreateFriendshipInvitation() {
        Member invitingMember = new Member();
        Member acceptingMember = new Member();
        FriendshipInvitation invitation = new FriendshipInvitation(invitingMember, acceptingMember);

        when(friendshipInvitationRepository.findByInvitingMemberAndAcceptingMember(invitingMember, acceptingMember)).thenReturn(Optional.empty());

        friendshipService.createFriendshipInvitation(invitingMember, acceptingMember);

        verify(friendshipInvitationRepository, times(1)).save(any(FriendshipInvitation.class));
    }

    @Test
    void testSave() {
        FriendshipInvitation invitation = new FriendshipInvitation();
        friendshipService.save(invitation);

        verify(friendshipInvitationRepository, times(1)).save(invitation);
    }

    @Test
    void testAcceptFriendshipInvitation() {
        FriendshipInvitation invitation = new FriendshipInvitation();
        when(friendshipInvitationRepository.findById(anyLong())).thenReturn(Optional.of(invitation));

        friendshipService.acceptFriendshipInvitation(1L);

        assertTrue(invitation.isAccepted());
        verify(friendshipInvitationRepository, times(1)).save(invitation);
    }

    @Test
    void testDeclineFriendshipInvitation() {
        doNothing().when(friendshipInvitationRepository).deleteById(anyLong());

        friendshipService.declineFriendshipInvitation(1L);

        verify(friendshipInvitationRepository, times(1)).deleteById(1L);
    }
}
