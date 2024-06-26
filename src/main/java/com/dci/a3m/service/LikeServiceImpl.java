package com.dci.a3m.service;

import com.dci.a3m.entity.Comment;
import com.dci.a3m.entity.Like;
import com.dci.a3m.entity.Member;
import com.dci.a3m.entity.Post;
import com.dci.a3m.repository.LikeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LikeServiceImpl implements LikeService {

    private LikeRepository likeRepository;
    private PostService postService;
    private MemberService memberService;
    private CommentService commentService;

    @Autowired
    public LikeServiceImpl(LikeRepository likeRepository, PostService postService, MemberService memberService, CommentService commentService) {
        this.likeRepository = likeRepository;
        this.postService = postService;
        this.memberService = memberService;
        this.commentService = commentService;

    }

    @Override
    public List<Like> findAll() {
        return likeRepository.findAll();
    }

    @Override
    public Like findById(Long id) {
        Optional<Like> result = likeRepository.findById(id);

        Like like = null;

        if (result.isPresent()) {
            like = result.get();
        } else {
            throw new RuntimeException("Like with id " + id + " not found.");
        }
        return like;
    }

    @Override
    public Like findByMemberAndPost(Member member, Post post) {
        return likeRepository.findByMemberAndPost(member, post);
    }

    @Override
    public Like findByMemberAndComment(Member member, Comment comment) {
        return likeRepository.findByMemberAndComment(member, comment);
    }

    @Override
    public void save(Like like) {
        likeRepository.save(like);
    }

    @Override
    public void update(Like like) {
        if (like.getId() != null) {
            likeRepository.save(like);
        } else {
            throw new RuntimeException("Like id is null");
        }
    }

    @Override
    public void deleteById(Long id) {
        likeRepository.deleteById(id);
    }



    @Override
    public boolean hasMemberLikedPost(Member member, Post post) {
        return likeRepository.existsByMemberAndPost(member, post);
    }

    @Override
    public boolean hasMemberLikedComment(Member member, Comment comment) {
        return likeRepository.existsByMemberAndComment(member, comment);
    }


}

