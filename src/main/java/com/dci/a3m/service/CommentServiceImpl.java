package com.dci.a3m.service;

import com.dci.a3m.entity.Comment;
import com.dci.a3m.entity.Member;
import com.dci.a3m.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CommentServiceImpl implements CommentService{

    private CommentRepository commentRepository;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Override
    public List<Comment> findAll() {
        return commentRepository.findAll();
    }

    @Override
    public Comment findById(Long id) {
        Optional<Comment> result = commentRepository.findById(id);

        Comment comment = null;

        if(result.isPresent()){
            comment = result.get();
        }else{
            throw new RuntimeException("Comment with id " + id + " not found.");
        }
        return comment;
    }

    @Override
    public void save(Comment comment) {
        commentRepository.save(comment);
    }

    @Override
    public void update(Comment comment) {
        if (comment.getId() != null) {
            commentRepository.save(comment);
        } else {
            throw new RuntimeException("Comment id is null");
        }
    }

    @Override
    public void deleteById(Long id) {
        commentRepository.deleteById(id);
    }

    @Override
    public List<Comment> findAllByMember(Member member) {
        return commentRepository.findAllByMember(member);
    }
}