package com.dci.a3m.entityTest;

import com.dci.a3m.entity.FriendshipInvitation;
import com.dci.a3m.entity.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FriendshipInvitationTest {

    @Mock
    private Member invitingMember;

    @Mock
    private Member acceptingMember;

    @InjectMocks
    private FriendshipInvitation invitation;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        invitation = new FriendshipInvitation();
        setId(invitation, 1L); // Use reflection to set the id
        invitation.setInvitingMember(invitingMember);
        invitation.setAcceptingMember(acceptingMember);
        invitation.setAccepted(false);
    }

    private void setId(FriendshipInvitation invitation, Long id) {
        try {
            Field idField = FriendshipInvitation.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(invitation, id);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testInvitationFields() {
        assertEquals(1L, invitation.getId());
        assertEquals(invitingMember, invitation.getInvitingMember());
        assertEquals(acceptingMember, invitation.getAcceptingMember());
        assertEquals(false, invitation.isAccepted());
    }
}
