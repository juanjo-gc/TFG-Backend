package es.uca.tfg.backend.controller;
import es.uca.tfg.backend.config.AbstractTest;
import es.uca.tfg.backend.entity.Comment;
import es.uca.tfg.backend.entity.Event;
import es.uca.tfg.backend.entity.Province;
import es.uca.tfg.backend.entity.User;
import es.uca.tfg.backend.repository.CommentRepository;
import es.uca.tfg.backend.repository.EventRepository;
import es.uca.tfg.backend.repository.UserRepository;
import es.uca.tfg.backend.rest.CommentDTO;
import jakarta.validation.constraints.AssertTrue;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest
@RunWith(SpringRunner.class)
@ExtendWith(MockitoExtension.class)
public class CommentControllerTestUT extends AbstractTest {

    @Override
    @Before
    public void setUp() {
        super.setUp();
    }

    @Mock
    private CommentRepository _commentRepository;
    @Mock
    private EventRepository _eventrepository;
    @Mock
    private UserRepository _userRepository;

    @InjectMocks
    private CommentController _controller;

    @Mock
    private User _user;
    @Mock
    private Event _event;
    @Test
    public void newCommentWillCreateNewComment() {
        //given
        CommentDTO dto = new CommentDTO(1, 1, "Test comment");
        Mockito.when(_userRepository.findById(1)).thenReturn(Optional.of(_user));
        Mockito.when(_eventrepository.findById(1)).thenReturn(Optional.of(_event));
        Mockito.when(_commentRepository.save(any(Comment.class))).thenReturn(new Comment(dto.get_sText(), _user, _event));
        //when
        Comment comment = _controller.newComment(dto);
        //then
        Mockito.verify(_commentRepository).save(any(Comment.class));
        Assertions.assertEquals("Test comment", comment.get_sText());
    }

    @Test
    public void newCommentWillFailAsEventOrUserDoesNotExist() {
        //given
        CommentDTO dto = new CommentDTO(1, 1, "Test comment");
        Mockito.when(_commentRepository.save(any(Comment.class))).thenReturn(new Comment(dto.get_sText(), _user, _event));
        //when
        Comment comment = _controller.newComment(dto);
        //then
        Mockito.verify(_commentRepository, Mockito.times(0)).save(any(Comment.class));
        Assertions.assertEquals(0, comment.get_iId());
    }

    @Test
    public void getEventCommentsWillReturnListOfCommentsFromEvent() {
        //given
        List<Comment> aCommentsFromEvent = new ArrayList<>();
        for(int i = 0; i < 5; i++)
            aCommentsFromEvent.add(new Comment());
        Mockito.when(_eventrepository.findById(1)).thenReturn(Optional.of(_event));
        Mockito.when(_commentRepository.findOrderedCommentsByDatetime(_event)).thenReturn(aCommentsFromEvent);
        //when
        List<Comment> aComments = _controller.getEventComments(1);
        //then
        Mockito.verify(_commentRepository).findOrderedCommentsByDatetime(_event);
        Assertions.assertEquals(5, aComments.size());
    }

    @Test
    public void getEventCommentsWillFailAsEventDoesNotExist() {
        //given
        List<Comment> aCommentsFromEvent = new ArrayList<>();
        for(int i = 0; i < 5; i++)
            aCommentsFromEvent.add(new Comment());
        Mockito.when(_commentRepository.findOrderedCommentsByDatetime(_event)).thenReturn(aCommentsFromEvent);
        //when
        List<Comment> aComments = _controller.getEventComments(1);
        //then
        Mockito.verify(_commentRepository, Mockito.times(0)).findOrderedCommentsByDatetime(_event);
        Assertions.assertEquals(0, aComments.size());
    }

    @Test
    public void softDeleteOrRestoreCommentWillDeleteExistingComment() {
        //given
        Comment comment = new Comment("Test comment", _user, _event);
        Mockito.when(_commentRepository.findById(1)).thenReturn(Optional.of(comment));
        Mockito.when(_commentRepository.save(comment)).thenReturn(comment);
        //when
        boolean bHasBeenDeleted = _controller.softDeleteOrRestoreComment(1);
        //then
        Mockito.verify(_commentRepository).save(comment);
        Assertions.assertNotNull(comment.get_tDeleteDate());
        Assertions.assertTrue(bHasBeenDeleted);
    }

    @Test
    public void softDeleteOrRestoreCommentWillRestoreDeletedComment() {
        //given
        Comment comment = new Comment("Test comment", _user, _event);
        comment.set_tDeleteDate(LocalDateTime.now());
        Mockito.when(_commentRepository.findById(1)).thenReturn(Optional.of(comment));
        Mockito.when(_commentRepository.save(comment)).thenReturn(comment);
        //when
        boolean bHasBeenDeleted = _controller.softDeleteOrRestoreComment(1);
        //then
        Mockito.verify(_commentRepository).save(comment);
        Assertions.assertNull(comment.get_tDeleteDate());
        Assertions.assertFalse(bHasBeenDeleted);
    }

    @Test
    public void softDeleteOrRestoreCommentWillFailAsCommentDoesNotExist() {
        //given
        Comment comment = new Comment("Test comment", _user, _event);
        Mockito.when(_commentRepository.save(comment)).thenReturn(comment);
        //when
        boolean bHasBeenDeleted = _controller.softDeleteOrRestoreComment(1);
        //then
        Mockito.verify(_commentRepository, Mockito.times(0)).save(comment);
        Assertions.assertFalse(bHasBeenDeleted);
    }
}
