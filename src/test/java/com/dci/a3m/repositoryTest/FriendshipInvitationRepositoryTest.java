package com.dci.a3m.repositoryTest;

import com.dci.a3m.entity.FriendshipInvitation;
import com.dci.a3m.entity.Member;
import com.dci.a3m.repository.FriendshipInvitationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FriendshipInvitationRepositoryTest {

    @Mock
    private FriendshipInvitationRepository friendshipInvitationRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindByInvitingMemberOrAcceptingMemberAndAccepted() {
        Member member = new Member();
        Member member1 = new Member();
        List<FriendshipInvitation> invitations = List.of(new FriendshipInvitation(), new FriendshipInvitation());

        when(friendshipInvitationRepository.findByInvitingMemberOrAcceptingMemberAndAccepted(member, member1, true)).thenReturn(invitations);

        List<FriendshipInvitation> result = friendshipInvitationRepository.findByInvitingMemberOrAcceptingMemberAndAccepted(member, member1, true);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(friendshipInvitationRepository, times(1)).findByInvitingMemberOrAcceptingMemberAndAccepted(member, member1, true);
    }

    @Test
    void testFindByInvitingMemberAndAccepted() {
        Member member = new Member();
        List<FriendshipInvitation> invitations = List.of(new FriendshipInvitation(), new FriendshipInvitation());

        when(friendshipInvitationRepository.findByInvitingMemberAndAccepted(member, true)).thenReturn(invitations);

        List<FriendshipInvitation> result = friendshipInvitationRepository.findByInvitingMemberAndAccepted(member, true);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(friendshipInvitationRepository, times(1)).findByInvitingMemberAndAccepted(member, true);
    }

    @Test
    void testFindByAcceptingMemberAndAccepted() {
        Member member = new Member();
        List<FriendshipInvitation> invitations = List.of(new FriendshipInvitation(), new FriendshipInvitation());

        when(friendshipInvitationRepository.findByAcceptingMemberAndAccepted(member, true)).thenReturn(invitations);

        List<FriendshipInvitation> result = friendshipInvitationRepository.findByAcceptingMemberAndAccepted(member, true);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(friendshipInvitationRepository, times(1)).findByAcceptingMemberAndAccepted(member, true);
    }

    @Test
    void testFindByInvitingMemberAndAcceptingMember() {
        Member invitingMember = new Member();
        Member acceptingMember = new Member();
        FriendshipInvitation invitation = new FriendshipInvitation();

        when(friendshipInvitationRepository.findByInvitingMemberAndAcceptingMember(invitingMember, acceptingMember)).thenReturn(Optional.of(invitation));

        Optional<Object> result = friendshipInvitationRepository.findByInvitingMemberAndAcceptingMember(invitingMember, acceptingMember);

        assertTrue(result.isPresent());
        verify(friendshipInvitationRepository, times(1)).findByInvitingMemberAndAcceptingMember(invitingMember, acceptingMember);
    }

    @Test
    void testFindByInvitingMemberAndAcceptingMemberAndAccepted() {
        Member invitingMember = new Member();
        Member acceptingMember = new Member();
        List<FriendshipInvitation> invitations = List.of(new FriendshipInvitation(), new FriendshipInvitation());

        when(friendshipInvitationRepository.findByInvitingMemberAndAcceptingMemberAndAccepted(invitingMember, acceptingMember, true)).thenReturn(invitations);

        List<FriendshipInvitation> result = friendshipInvitationRepository.findByInvitingMemberAndAcceptingMemberAndAccepted(invitingMember, acceptingMember, true);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(friendshipInvitationRepository, times(1)).findByInvitingMemberAndAcceptingMemberAndAccepted(invitingMember, acceptingMember, true);
    }
}
