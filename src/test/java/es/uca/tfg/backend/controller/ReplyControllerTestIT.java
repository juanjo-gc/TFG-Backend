package es.uca.tfg.backend.controller;
import es.uca.tfg.backend.config.AbstractTest;
import es.uca.tfg.backend.entity.Person;
import es.uca.tfg.backend.entity.Reply;
import es.uca.tfg.backend.entity.Ticket;
import es.uca.tfg.backend.rest.ReplyDTO;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;


@WebMvcTest
@RunWith(SpringRunner.class)
public class ReplyControllerTestIT extends AbstractTest {

    @Override
    @Before
    public void setUp() {
        super.setUp();
    }


    @Test
    public void newReplyWillCreateNewReply() throws Exception{
        //given
        Ticket ticket = new Ticket();
        Person person = new Person();
        ReplyDTO dto = new ReplyDTO(1, "Test reply", 1);
        Reply reply = new Reply(dto.get_sText(), person, ticket);
        Mockito.when(_personRepository.findById(1)).thenReturn(Optional.of(person));
        Mockito.when(_ticketRepository.findById(1)).thenReturn(Optional.of(ticket));
        Mockito.when(_replyRepository.save(any(Reply.class))).thenReturn(reply);
        //when
        ResultActions response = _mockMvc.perform(post("/api/newReply")
                .contentType(MediaType.APPLICATION_JSON)
                .content(_objectMapper.writeValueAsString(dto)));
        //then
        response.andDo(print())
                .andExpect(status().is2xxSuccessful());
        Mockito.verify(_ticketRepository).findById(1);
        Mockito.verify(_personRepository).findById(1);
        Assertions.assertEquals(dto.get_sText(), _objectMapper.readValue(response.andReturn().getResponse().getContentAsString(), Reply.class).get_sText());
    }

    @Test
    public void newReplyWillFailAndReturnEmptyReplyAsUserOrTicketDoesNotExist() throws Exception{
        //given
        Ticket ticket = new Ticket();
        Person person = new Person();
        ReplyDTO dto = new ReplyDTO(1, "Test reply", 1);
        Reply reply = new Reply(dto.get_sText(), person, ticket);
        Mockito.when(_ticketRepository.findById(1)).thenReturn(Optional.of(ticket));
        Mockito.when(_replyRepository.save(any(Reply.class))).thenReturn(reply);
        //when
        ResultActions response = _mockMvc.perform(post("/api/newReply")
                .contentType(MediaType.APPLICATION_JSON)
                .content(_objectMapper.writeValueAsString(dto)));
        //then
        response.andDo(print())
                .andExpect(status().is2xxSuccessful());
        Mockito.verify(_ticketRepository).findById(1);
        Mockito.verify(_personRepository).findById(1);
        Assertions.assertNull(_objectMapper.readValue(response.andReturn().getResponse().getContentAsString(), Reply.class).get_sText());
    }

    @Test
    public void getTicketRepliesWillReturnReplyList() throws Exception {
        //given
        Ticket ticket = Mockito.mock(Ticket.class);
        List<Reply> aReplies = List.of(new Reply(), new Reply());
        Mockito.when(_ticketRepository.findById(1)).thenReturn(Optional.of(ticket));
        Mockito.when(_replyRepository.findByTicketOrderByCreatedAtAsc(ticket)).thenReturn(aReplies);
        //when
        ResultActions response = _mockMvc.perform(get("/api/getTicketReplies/1"));
        //then
        response.andDo(print())
                .andExpect(status().is2xxSuccessful());
        Mockito.verify(_ticketRepository).findById(1);
        Mockito.verify(_replyRepository).findByTicketOrderByCreatedAtAsc(ticket);
        Assertions.assertEquals(2, _objectMapper.readValue(response.andReturn().getResponse().getContentAsString(), List.class).size());
    }

    @Test
    public void getTicketRepliesWillFailAndReturnEmptyListAsTicketDoesNotExist() throws Exception {
        //given
        Ticket ticket = Mockito.mock(Ticket.class);
        List<Reply> aReplies = List.of(new Reply(), new Reply());
        Mockito.when(_replyRepository.findByTicketOrderByCreatedAtAsc(ticket)).thenReturn(aReplies);
        //when
        ResultActions response = _mockMvc.perform(get("/api/getTicketReplies/1"));
        //then
        response.andDo(print())
                .andExpect(status().is2xxSuccessful());
        Mockito.verify(_ticketRepository).findById(1);
        Mockito.verify(_replyRepository, Mockito.times(0)).findByTicketOrderByCreatedAtAsc(ticket);
        Assertions.assertEquals(0, _objectMapper.readValue(response.andReturn().getResponse().getContentAsString(), List.class).size());
    }
}
