package es.uca.tfg.backend.controller;


import es.uca.tfg.backend.config.AbstractTest;
import es.uca.tfg.backend.entity.Comment;
import es.uca.tfg.backend.entity.Event;
import es.uca.tfg.backend.entity.Message;
import es.uca.tfg.backend.entity.User;
import es.uca.tfg.backend.rest.CommentDTO;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@RunWith(SpringRunner.class)
public class CommentControllerTest extends AbstractTest {

    @Override
    @Before
    public void setUp() {
        super.setUp();
    }

    @Test
    public void newCommentTest() throws Exception {
        //given
        User user = Mockito.mock(User.class);
        Event event = Mockito.mock(Event.class);
        String sText = "Test";
        CommentDTO commentDTO = new CommentDTO(1, 2, sText);
        Mockito.when(user.get_iId()).thenReturn(1);
        Mockito.when(event.get_iId()).thenReturn(2);
        Mockito.when(_userRepository.findById(1)).thenReturn(Optional.of(user));
        Mockito.when(_eventRepository.findById(2)).thenReturn(Optional.of(event));
        //when
        ResultActions response = _mockMvc.perform(post("/api/newComment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(_objectMapper.writeValueAsString(commentDTO)));
        //then
        response.andDo(print())
                .andExpect(status().is2xxSuccessful());
        Mockito.verify(_commentRepository).save(any(Comment.class));
    }

    @Test
    public void getEventCommentsTest() throws Exception {
        //given
        Event event = Mockito.mock(Event.class);
        Comment comment = Mockito.mock(Comment.class);
        List<Comment> aComments = new ArrayList<>();
        Mockito.when(event.get_iId()).thenReturn(1);
        Mockito.when(_eventRepository.findById(1)).thenReturn(Optional.of(event));
        Mockito.when(_commentRepository.findOrderedCommentsByDatetime(any(Event.class))).thenReturn(aComments);
        //when
        ResultActions response = _mockMvc.perform(get("/api/getEventComments/1"));
        //then
        response.andDo(print())
                .andExpect(status().is2xxSuccessful());
        Assertions.assertEquals(aComments, _objectMapper.readValue(response.andReturn().getResponse().getContentAsString(), List.class));
    }
}
