package com.dci.a3m.repository;

import com.dci.a3m.entity.FriendshipInvitation;
import com.dci.a3m.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FriendshipInvitationRepository extends JpaRepository<FriendshipInvitation, Long> {


    // READ FRIENDS
    List<FriendshipInvitation> findByInvitingMemberOrAcceptingMemberAndAccepted(Member member, Member member1, boolean b);

    // READ INVITATIONS BY INVITING MEMBER
    List<FriendshipInvitation> findByInvitingMemberAndAccepted(Member invitingMember, boolean accepted);

    // READ INVITATIONS BY ACCEPTING MEMBER
    List<FriendshipInvitation> findByAcceptingMemberAndAccepted(Member acceptingMember, boolean accepted);


    // READ INVITATIONS BY INVITING MEMBER AND ACCEPTING MEMBER
    Optional<Object> findByInvitingMemberAndAcceptingMember(Member invitingMember, Member acceptingMember);



        List<FriendshipInvitation> findByInvitingMemberAndAcceptingMemberAndAccepted(Member invitingMember, Member acceptingMember, boolean accepted);
}
