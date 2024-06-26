package com.dci.a3m.service;

import com.dci.a3m.entity.FriendshipInvitation;
import com.dci.a3m.entity.Member;
import com.dci.a3m.repository.FriendshipInvitationRepository;
import com.dci.a3m.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class FriendshipServiceImpl implements FriendshipService {

    private final FriendshipInvitationRepository friendshipInvitationRepository;
    private final MemberRepository memberRepository;

    @Autowired
    public FriendshipServiceImpl(FriendshipInvitationRepository friendshipInvitationRepository, MemberRepository memberRepository) {
        this.friendshipInvitationRepository = friendshipInvitationRepository;
        this.memberRepository = memberRepository;
    }


    // CRUD OPERATIONS

    // READ

    @Override
    public List<FriendshipInvitation> findByAcceptingMemberAndNotAccepted(Member member) {
        return friendshipInvitationRepository.findByAcceptingMemberAndAccepted(member, false);
    }

    @Override
    public List<FriendshipInvitation> findByInvitingMemberAndNotAccepted(Member member) {
        return friendshipInvitationRepository.findByInvitingMemberAndAccepted(member, false);
    }

    @Override
    public List<FriendshipInvitation> findFriendsAccepted(Member member) {
        return friendshipInvitationRepository.findByInvitingMemberOrAcceptingMemberAndAccepted(member, member, true);
    }

    @Override
    public FriendshipInvitation findById(Long friendshipId) {
        Optional<FriendshipInvitation> result = friendshipInvitationRepository.findById(friendshipId);
        FriendshipInvitation friendshipInvitation = null;
        if (result.isPresent()) {
            friendshipInvitation = result.get();
        } else {
            throw new RuntimeException("Did not find friendship invitation with id - " + friendshipId);
        } return friendshipInvitation;
    }

    @Override
    public List<FriendshipInvitation> findAll() {
        return friendshipInvitationRepository.findAll();
    }


    // CREATE

    @Override
    @Transactional
    public void createFriendshipInvitation(Member invitingMember, Member acceptingMember) {
        if (friendshipInvitationRepository.findByInvitingMemberAndAcceptingMember(invitingMember, acceptingMember).isPresent()) {
            throw new RuntimeException("Friendship request already sent");
        }
        FriendshipInvitation friendshipInvitation = new FriendshipInvitation(invitingMember, acceptingMember);
        friendshipInvitationRepository.save(friendshipInvitation);
    }

    @Override
    public void save(FriendshipInvitation friendshipInvitation) {
        friendshipInvitationRepository.save(friendshipInvitation);
    }


    // UPDATE

    @Override
    @Transactional
    public void acceptFriendshipInvitation(Long id) {
        FriendshipInvitation friendshipInvitation = friendshipInvitationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Friendship with id " + id + " not found."));
        friendshipInvitation.setAccepted(true);
        friendshipInvitationRepository.save(friendshipInvitation);
    }


    // DELETE
    @Override
    public void declineFriendshipInvitation(Long id) {
        friendshipInvitationRepository.deleteById(id);
    }


}
