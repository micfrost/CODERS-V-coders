package com.dci.a3m.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "friendship_invitations")
public class FriendshipInvitation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "invitingMember_id")
    private Member invitingMember;

    @ManyToOne
    @JoinColumn(name = "acceptingMember_id")
    private Member acceptingMember;

    private boolean accepted;

    // Default constructor
    public FriendshipInvitation() {}

    // Constructor with parameters
    public FriendshipInvitation(Member invitingMember, Member acceptingMember) {
        this.invitingMember = invitingMember;
        this.acceptingMember = acceptingMember;
        this.accepted = false; // Default value
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public Member getInvitingMember() {
        return invitingMember;
    }

    public void setInvitingMember(Member invitingMember) {
        this.invitingMember = invitingMember;
    }

    public Member getAcceptingMember() {
        return acceptingMember;
    }

    public void setAcceptingMember(Member acceptingMember) {
        this.acceptingMember = acceptingMember;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }
}
