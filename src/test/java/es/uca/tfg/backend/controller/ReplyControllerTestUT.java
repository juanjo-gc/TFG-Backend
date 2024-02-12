package es.uca.tfg.backend.controller;
import es.uca.tfg.backend.config.AbstractTest;
import es.uca.tfg.backend.entity.Person;
import es.uca.tfg.backend.entity.Reply;
import es.uca.tfg.backend.entity.Ticket;
import es.uca.tfg.backend.repository.PersonRepository;
import es.uca.tfg.backend.repository.ReplyRepository;
import es.uca.tfg.backend.repository.TicketRepository;
import es.uca.tfg.backend.rest.ReplyDTO;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.ArgumentMatchers.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@WebMvcTest
@RunWith(SpringRunner.class)
@ExtendWith(MockitoExtension.class)
public class ReplyControllerTestUT extends AbstractTest {

    @Override
    @Before
    public void setUp() {
        super.setUp();
    }

    @Mock
    private PersonRepository _personRepository;
    @Mock
    private TicketRepository _ticketRepository;
    @Mock
    private ReplyRepository _replyRepository;
    @InjectMocks
    private ReplyController _controller;

    @Test
    public void newReplyWillCreateNewReply() {
        //given
        Ticket ticket = Mockito.mock(Ticket.class);
        Person person = Mockito.mock(Person.class);
        Reply reply = Mockito.mock(Reply.class);
        ReplyDTO dto = new ReplyDTO(1, "Test Reply", 1);
        Mockito.when(_personRepository.findById(1)).thenReturn(Optional.of(person));
        Mockito.when(_ticketRepository.findById(1)).thenReturn(Optional.of(ticket));
        Mockito.when(_replyRepository.save(any(Reply.class))).thenReturn(reply);
        Mockito.when(reply.get_iId()).thenReturn(10);
        //when
        Reply newReply = _controller.newReply(dto);
        //then
        Assertions.assertEquals(10, newReply.get_iId());
    }

    @Test
    public void newReplyWillFailAndReturnEmptyReply() {
        //given
        Ticket ticket = Mockito.mock(Ticket.class);
        Person person = Mockito.mock(Person.class);
        Reply reply = Mockito.mock(Reply.class);
        ReplyDTO dto = new ReplyDTO(1, "Test Reply", 1);
        Mockito.when(_personRepository.findById(1)).thenReturn(Optional.of(person));
        Mockito.when(_replyRepository.save(any(Reply.class))).thenReturn(reply);
        Mockito.when(reply.get_iId()).thenReturn(10);
        //when
        Reply newReply = _controller.newReply(dto);
        //then
        Assertions.assertEquals(0, newReply.get_iId());
    }

    @Test
    public void getTicketRepliesWillReturnRepliesList() {
        //given
        Ticket ticket = Mockito.mock(Ticket.class);
        List<Reply> aReplies = List.of(new Reply(), new Reply(), new Reply());
        Mockito.when(_ticketRepository.findById(1)).thenReturn(Optional.of(ticket));
        Mockito.when(_replyRepository.findByTicketOrderByCreatedAtAsc(ticket)).thenReturn(aReplies);
        //when
        List<Reply> aTicketReplies = _controller.getTicketReplies(1);
        //then
        Assertions.assertEquals(3, aTicketReplies.size());
    }

    @Test
    public void getTicketRepliesWillFailAndReturnEmptyList() {
        //given
        Ticket ticket = Mockito.mock(Ticket.class);
        List<Reply> aReplies = List.of(new Reply(), new Reply(), new Reply());
        Mockito.when(_replyRepository.findByTicketOrderByCreatedAtAsc(ticket)).thenReturn(aReplies);
        //when
        List<Reply> aTicketReplies = _controller.getTicketReplies(1);
        //then
        Assertions.assertEquals(0, aTicketReplies.size());
    }
}
