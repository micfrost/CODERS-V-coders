package com.dci.a3m.databaseLoaderTest;

import com.dci.a3m.entity.Member;
import com.dci.a3m.entity.User;
import com.dci.a3m.entity.FriendshipInvitation;
import com.dci.a3m.repository.MemberRepository;
import com.dci.a3m.service.AdminService;
import com.dci.a3m.service.FriendshipService;
import com.dci.a3m.service.UserService;
import com.dci.a3m.databaseLoader.DatabaseLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class DatabaseLoaderTest {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AdminService adminService;

    @Autowired
    private UserService userService;

    @Autowired
    private FriendshipService friendshipService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private DatabaseLoader databaseLoader;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testInitAdmin() {
        try {
            databaseLoader.run();
            User admin = userService.findByUsername("AdminExample");
            assertNotNull(admin, "Admin should be initialized");
            assertEquals("admin@example.com", admin.getEmail(), "Admin email should match");
        } catch (Exception e) {
            fail("Exception should not be thrown: " + e.getMessage());
        }
    }

    @Test
    public void testInitMembers() {
        try {
            databaseLoader.run();
            List<Member> members = memberRepository.findAll();
            assertEquals(4, members.size(), "There should be 4 members initialized");

            Member member1 = userService.findByUsername("AliceRiver").getMember();
            assertNotNull(member1, "Member 1 should be initialized");
            assertEquals("Alice", member1.getFirstName(), "Member 1 first name should match");
            assertEquals(3, member1.getPosts().size(), "Member 1 should have 3 posts");
        } catch (Exception e) {
            fail("Exception should not be thrown: " + e.getMessage());
        }
    }

    @Test
    public void testInitFriendships() {
        try {
            databaseLoader.run();

            Member member1 = userService.findByUsername("AliceRiver").getMember();
            Member member2 = userService.findByUsername("ThomasLake").getMember();
            Member member3 = userService.findByUsername("WillymWoods").getMember();

            List<FriendshipInvitation> friendsOfMember1 = friendshipService.findFriendsAccepted(member1);
            assertTrue(friendsOfMember1.stream().anyMatch(invitation ->
                            invitation.getInvitingMember().equals(member1) && invitation.getAcceptingMember().equals(member2)),
                    "Member 1 should be friends with Member 2");

            List<FriendshipInvitation> pendingFriendsOfMember3 = friendshipService.findByAcceptingMemberAndNotAccepted(member3);
            assertTrue(pendingFriendsOfMember3.stream().anyMatch(invitation ->
                            invitation.getInvitingMember().equals(member3) && invitation.getAcceptingMember().equals(member1)),
                    "Member 3 should have a pending friendship with Member 1");
        } catch (Exception e) {
            fail("Exception should not be thrown: " + e.getMessage());
        }
    }
}
