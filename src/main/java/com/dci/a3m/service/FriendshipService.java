package com.dci.a3m.service;

import com.dci.a3m.entity.FriendshipInvitation;
import com.dci.a3m.entity.Member;

import java.util.List;
import java.util.Map;

public interface FriendshipService {

    // CRUD OPERATIONS

    // READ
    List<FriendshipInvitation> findByAcceptingMemberAndNotAccepted(Member member);

    List<FriendshipInvitation> findByInvitingMemberAndNotAccepted(Member member);

    List<FriendshipInvitation> findFriendsAccepted(Member member);


    // CREATE
    void createFriendshipInvitation(Member invitingMember, Member acceptingMember);

    void save(FriendshipInvitation friendshipInvitation);


    // UPDATE
    void acceptFriendshipInvitation(Long id);


    // DELETE
    void declineFriendshipInvitation(Long id);


    FriendshipInvitation findById(Long friendshipId);

    List<FriendshipInvitation> findAll();}
