package com.dci.a3m.repository;

import com.dci.a3m.entity.Comment;
import com.dci.a3m.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByMember(Member member);
}
