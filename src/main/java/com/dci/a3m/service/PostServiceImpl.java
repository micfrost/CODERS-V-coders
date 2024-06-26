package com.dci.a3m.service;

import com.dci.a3m.entity.Comment;
import com.dci.a3m.entity.Post;
import com.dci.a3m.entity.Member;
import com.dci.a3m.repository.CommentRepository;
import com.dci.a3m.repository.PostRepository;
import com.dci.a3m.repository.MemberRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final CommentRepository commentRepository;

    public PostServiceImpl(PostRepository postRepository, MemberRepository memberRepository, CommentRepository commentRepository) {
        this.postRepository = postRepository;
        this.memberRepository = memberRepository;
        this.commentRepository = commentRepository;
    }

    // CRUD OPERATIONS

    // READ ALL

    @Override
    public List<Post> findAll() {

        return postRepository.findAll();
    }

    // READ ALL BY MEMBER
    @Override
    public List<Post> findAllByMember(Member member) {

        // find all posts by member
        List<Post> posts = postRepository.findAllByMember(member);

        return posts;
    }


    // READ BY ID
    @Override
    public Post findById(Long id) {
        Optional<Post> result = postRepository.findById(id);

        Post post = null;

        if (result.isPresent()) {
            post = result.get();
        } else {
            throw new RuntimeException("Did not find post id - " + id);
        }
        return post;
    }


    // SAVE
    @Override
    public void save(Post post) {
        postRepository.save(post);

    }

    // UPDATE
    @Override
    public void update(Post post) {
        if (post.getId() != null) {
            postRepository.save(post);
        } else {
            throw new RuntimeException("Post id is null");
        }
    }

    // DELETE
    @Override
    public void deleteById(Long id) {
        postRepository.deleteById(id);
    }

    @Override
    public String getRandomMediaUrl() {
        String[] mediaUrls = {
                "https://images.unsplash.com/photo-1514477917009-389c76a86b68?q=80&w=1534&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                "https://images.unsplash.com/photo-1529697216570-f48ef8f6b2dd?q=80&w=1470&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                "https://images.unsplash.com/photo-1675967682194-2076e2b772e7?q=80&w=1632&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                "https://images.unsplash.com/photo-1428908728789-d2de25dbd4e2?q=80&w=1470&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                "https://images.unsplash.com/photo-1502512571217-6a08d302fe5a?q=80&w=1588&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                "https://images.unsplash.com/photo-1505322022379-7c3353ee6291?q=80&w=987&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                "https://images.unsplash.com/photo-1465080357990-d4bc259ec4a9?q=80&w=1587&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"
        };

        Random random = new Random();
        int index = random.nextInt(mediaUrls.length);
        return mediaUrls[index];
    }


}
