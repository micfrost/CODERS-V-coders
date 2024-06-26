package com.dci.a3m.service;

import com.dci.a3m.entity.Member;
import com.dci.a3m.entity.Post;

import java.util.List;

public interface PostService {
    List<Post> findAll();

    List<Post> findAllByMember(Member member);

    Post findById(Long id);
    void save(Post post);
    void update(Post post);
    void deleteById(Long id);

    String getRandomMediaUrl();
}
