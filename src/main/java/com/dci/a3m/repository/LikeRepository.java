package com.dci.a3m.repository;

import com.dci.a3m.entity.Comment;
import com.dci.a3m.entity.Like;
import com.dci.a3m.entity.Member;
import com.dci.a3m.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Like findByMemberAndPost(Member member, Post post);
    Like findByMemberAndComment(Member member, Comment comment);

    boolean existsByMemberAndPost(Member member, Post post);

    boolean existsByMemberAndComment(Member member, Comment comment);
}
