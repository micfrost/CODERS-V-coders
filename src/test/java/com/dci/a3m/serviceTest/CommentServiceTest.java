package com.dci.a3m.serviceTest;

import com.dci.a3m.entity.Comment;
import com.dci.a3m.entity.Member;
import com.dci.a3m.repository.CommentRepository;
import com.dci.a3m.service.CommentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private CommentServiceImpl commentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAll() {
        Comment comment1 = new Comment();
        Comment comment2 = new Comment();
        List<Comment> comments = Arrays.asList(comment1, comment2);

        when(commentRepository.findAll()).thenReturn(comments);

        List<Comment> result = commentService.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void testFindById() {
        Comment comment = new Comment();
        when(commentRepository.findById(anyLong())).thenReturn(Optional.of(comment));

        Comment result = commentService.findById(1L);

        assertNotNull(result);
        assertEquals(comment, result);
    }

    @Test
    void testSave() {
        Comment comment = new Comment();
        commentService.save(comment);

        verify(commentRepository, times(1)).save(comment);
    }

    @Test
    void testUpdate() {
        Comment comment = new Comment();
        comment.setId(1L);
        commentService.update(comment);

        verify(commentRepository, times(1)).save(comment);
    }

    @Test
    void testUpdateThrowsExceptionWhenIdIsNull() {
        Comment comment = new Comment();

        Exception exception = assertThrows(RuntimeException.class, () -> {
            commentService.update(comment);
        });

        String expectedMessage = "Comment id is null";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void testDeleteById() {
        doNothing().when(commentRepository).deleteById(anyLong());

        commentService.deleteById(1L);

        verify(commentRepository, times(1)).deleteById(1L);
    }

    @Test
    void testFindAllByMember() {
        Member member = new Member();
        Comment comment1 = new Comment();
        Comment comment2 = new Comment();
        List<Comment> comments = Arrays.asList(comment1, comment2);

        when(commentRepository.findAllByMember(any(Member.class))).thenReturn(comments);

        List<Comment> result = commentService.findAllByMember(member);

        assertNotNull(result);
        assertEquals(2, result.size());
    }
}
