package com.dci.a3m.service;

import com.dci.a3m.entity.Comment;
import com.dci.a3m.entity.Like;
import com.dci.a3m.entity.Member;
import com.dci.a3m.entity.Post;

import java.util.List;

public interface LikeService {
    List<Like> findAll();
    Like findById(Long id);
    void save(Like like);
    void update(Like like);
    void deleteById(Long id);


    Like findByMemberAndPost(Member member, Post post);
    Like findByMemberAndComment(Member member, Comment comment);

    boolean hasMemberLikedPost(Member member, Post post);

    boolean hasMemberLikedComment(Member member, Comment comment);
}
