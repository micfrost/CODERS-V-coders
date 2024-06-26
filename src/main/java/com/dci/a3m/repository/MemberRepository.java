package com.dci.a3m.repository;

import com.dci.a3m.entity.Member;
import com.dci.a3m.entity.User;
import org.hibernate.mapping.List;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>{
    Optional<Member> findByUser_Username(String username);

    Member findByUser_Email(String email);

    Member findByUser(User user);
}
