package com.dci.a3m.repositoryTest;

import com.dci.a3m.entity.Comment;
import com.dci.a3m.entity.Member;
import com.dci.a3m.repository.CommentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CommentRepositoryTest {

    @Mock
    private CommentRepository commentRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindById() {
        Long commentId = 1L;
        Comment comment = new Comment();
        comment.setId(commentId);
        comment.setContent("Test comment");

        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));

        Optional<Comment> foundComment = commentRepository.findById(commentId);

        assertTrue(foundComment.isPresent());
        assertEquals(comment.getId(), foundComment.get().getId());
        assertEquals(comment.getContent(), foundComment.get().getContent());
        verify(commentRepository, times(1)).findById(commentId);
    }

    @Test
    void testSave() {
        Comment comment = new Comment();
        comment.setContent("Really appreciated.");

        when(commentRepository.save(comment)).thenReturn(comment);

        Comment savedComment = commentRepository.save(comment);

        assertNotNull(savedComment);
        assertEquals(comment.getContent(), savedComment.getContent());
        verify(commentRepository, times(1)).save(comment);
    }

    @Test
    void testDelete() {
        Long commentId = 1L;

        doNothing().when(commentRepository).deleteById(commentId);

        commentRepository.deleteById(commentId);

        verify(commentRepository, times(1)).deleteById(commentId);
    }

    @Test
    void testFindAllByMember() {
        Member member = new Member();
        List<Comment> comments = new ArrayList<>();
        Comment comment1 = new Comment();
        comment1.setContent("Hava a lovely day. See yeah.");
        Comment comment2 = new Comment();
        comment2.setContent("Thanks for reading my comment. Kudos.");
        comments.add(comment1);
        comments.add(comment2);

        when(commentRepository.findAllByMember(member)).thenReturn(comments);

        List<Comment> foundComments = commentRepository.findAllByMember(member);

        assertNotNull(foundComments);
        assertEquals(2, foundComments.size());
        assertEquals("Hava a lovely day. See yeah.", foundComments.get(0).getContent());
        assertEquals("Thanks for reading my comment. Kudos.", foundComments.get(1).getContent());
        verify(commentRepository, times(1)).findAllByMember(member);
    }
}
