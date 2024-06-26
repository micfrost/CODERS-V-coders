package com.dci.a3m.service;

import com.dci.a3m.entity.Comment;
import com.dci.a3m.entity.Member;
import com.dci.a3m.entity.Post;

import java.util.List;

public interface CommentService{

    List<Comment> findAll();
    Comment findById(Long id);
    void save(Comment comment);
    void update(Comment comment);
    void deleteById(Long id);

    List<Comment> findAllByMember(Member member);
}
