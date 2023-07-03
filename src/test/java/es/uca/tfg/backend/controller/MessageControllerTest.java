package es.uca.tfg.backend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.uca.tfg.backend.config.AbstractTest;
import es.uca.tfg.backend.entity.Message;
import es.uca.tfg.backend.entity.User;
import es.uca.tfg.backend.repository.MessageRepository;
import es.uca.tfg.backend.rest.MessageDTO;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.Result;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.context.WebApplicationContext;

import java.sql.Array;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
@RunWith(SpringRunner.class)
public class MessageControllerTest extends AbstractTest {

    @Override
    @Before
    public void setUp() {
        super.setUp();
    }

    //public MessageDTO(int iIssuerId, int iRecipientId, String sText)

    @Test
    public void saveNewMessageTest() throws Exception {
        //given
        User issuer = Mockito.mock(User.class);
        User recipient = Mockito.mock(User.class);
        Mockito.when(_userRepository.findBy_iId(0)).thenReturn(issuer);
        Mockito.when(_userRepository.findBy_iId(1)).thenReturn(recipient);
        Mockito.when(issuer.get_iId()).thenReturn(0);
        Mockito.when(recipient.get_iId()).thenReturn(1);
        MessageDTO messageDTO = new MessageDTO(issuer.get_iId(), recipient.get_iId(), "test");
        //when
        ResultActions response = _mockMvc.perform(post("/api/newMessage")
                .contentType(MediaType.APPLICATION_JSON)
                .content(_objectMapper.writeValueAsString(messageDTO)));
        //then
        response.andDo(print())
                .andExpect(status().is2xxSuccessful());
        Mockito.verify(_messageRepository).save(any(Message.class));
    }

    @Test
    public void getLastMessagesWillReturnEmptyList() throws Exception {
        //given
        User user = Mockito.mock(User.class);
        Mockito.when(_userRepository.findById(anyInt())).thenReturn(Optional.of(user));
        Mockito.when(_messageRepository.findMessagedUsers(user)).thenReturn(Collections.emptyList());
        Mockito.when(_messageRepository.findUserWhoMessagedCurrentUser(user)).thenReturn(Collections.emptyList());
        //when
        ResultActions response = _mockMvc.perform(get("/api/getLastMessages/0"));
        //then
        response.andDo(print())
                .andExpect(status().is2xxSuccessful());
        Assertions.assertEquals(Collections.emptyList(), _objectMapper.readValue(response.andReturn().getResponse().getContentAsString(), List.class));
        Mockito.verify(_messageRepository).findUserWhoMessagedCurrentUser(any(User.class));
        Mockito.verify(_messageRepository).findMessagedUsers(any(User.class));
    }

    /*
    @Test
    public void getLastMessagesWillReturnSomeMessages() throws Exception {
        //given
        User user = Mockito.mock(User.class);
        Page<Message> lastMessage = Mockito.mock(Page.class);
        Stream<Message> messageStream = Mockito.mock(Stream.class);
        Message message = Mockito.mock(Message.class);
        Optional<Message> optionalMessage = Optional.of(message);
        ArrayList<User> aUsers = new ArrayList<>();
        for(int i = 0; i < 5; i++) {
            aUsers.add(Mockito.mock(User.class));
        }
        System.out.println("Tamaño de lista: " + aUsers.size());
        Mockito.when(_userRepository.findById(anyInt())).thenReturn(Optional.of(user));
        Mockito.when(_messageRepository.findMessagedUsers(user)).thenReturn(aUsers);
        Mockito.when(_messageRepository.findUserWhoMessagedCurrentUser(user)).thenReturn(aUsers);
        Mockito.when(_messageRepository.findLastIssuerRecipientMessage(any(), any(), any())).thenReturn(lastMessage);
        Mockito.when(lastMessage.get()).thenReturn(messageStream);
        Mockito.when(messageStream.findFirst()).thenReturn(optionalMessage);
        Mockito.when(message.get_issuer().get_sUsername()).thenReturn(anyString());

        //when
        ResultActions response = _mockMvc.perform(get("/api/getLastMessages/0"));
        //then
        response.andDo(print())
                .andExpect(status().is2xxSuccessful());
        Assertions.assertEquals(Collections.emptyList(), _objectMapper.readValue(response.andReturn().getResponse().getContentAsString(), List.class));
        Mockito.verify(_messageRepository).findUserWhoMessagedCurrentUser(any(User.class));
        Mockito.verify(_messageRepository).findMessagedUsers(any(User.class));
    }

     */

    @Test
    public void getLastMessagesWillReturnSomeMessages() throws Exception {
        User user = new User("example@gmail.com", "password", "username", "user", "name", new Date());
        User messagedUser = new User("messaged@gmail.com", "password", "messaged", "user", "name", new Date());
        List<User> users = new ArrayList<>();
        users.add(messagedUser);

        List<Message> messages = new ArrayList<>();
        messages.add(new Message());

        Mockito.when(_userRepository.findById(user.get_iId())).thenReturn(Optional.of(user));
        Mockito.when(_messageRepository.findMessagedUsers(user)).thenReturn(users);
        Mockito.when(_messageRepository.findUserWhoMessagedCurrentUser(user)).thenReturn(users);
        Mockito.when(_messageRepository.findLastIssuerRecipientMessage(user, messagedUser, PageRequest.of(0, 1))).thenReturn(new PageImpl<>(messages));

        ResultActions result = _mockMvc.perform(get("/api/getLastMessages/0"))
                .andExpect(status().isOk());

        String content = result.andReturn().getResponse().getContentAsString();
        List<Message> lastMessages = new ObjectMapper().readValue(content, new TypeReference<List<Message>>() {});

        Assertions.assertNotNull(lastMessages);
        Assertions.assertEquals(1, lastMessages.size());
    }

    @Test
    public void getConversationWillSuccess() throws Exception {
        //given
        User user = new User("example@gmail.com", "password", "username", "user", "name", new Date());
        User target = new User("messaged@gmail.com", "password", "messaged", "user", "name", new Date());
        Optional<User> optionalUser = Optional.of(user);
        Optional<User> optionalTarget = Optional.of(target);
        ArrayList<Message> aMessages = new ArrayList<>();
        for(int i = 0; i < 5; i++)
            aMessages.add(Mockito.mock(Message.class));

        Mockito.when(_userRepository.findById(0)).thenReturn(optionalUser);
        Mockito.when(_userRepository.findById(1)).thenReturn(optionalTarget);

        Mockito.when(_messageRepository.findConversation(user, target)).thenReturn(aMessages);
        //when
        ResultActions response = _mockMvc.perform(get("/api/getConversation/0/1"));
        //then
        response.andDo(print())
                .andExpect(status().is2xxSuccessful());
        Assertions.assertEquals(5, _objectMapper.readValue(response.andReturn().getResponse().getContentAsString(), List.class).size());
    }

    @Test
    public void testGetConversation() throws Exception {
        User user = new User("example@gmail.com", "password", "username", "user", "name", new Date());
        User target = new User("messaged@gmail.com", "password", "messaged", "user", "name", new Date());

        List<Message> aMessages = new ArrayList<>();
        for(int i = 0; i < 5; i++)
            aMessages.add(Mockito.mock(Message.class));

        Mockito.when(_userRepository.findById(user.get_iId())).thenReturn(Optional.of(user));
        Mockito.when(_userRepository.findById(target.get_iId())).thenReturn(Optional.of(target));
        Mockito.when(_messageRepository.findConversation(any(), any())).thenReturn(aMessages);

        ResultActions response = _mockMvc.perform(get("/api/getConversation/0/1"));

        response.andExpect(status().is2xxSuccessful());
        //List<Message> conversation = _objectMapper.readValue(response.andReturn().getResponse().getContentAsString(), List.class);

        List<Message> conversation = _objectMapper.readValue(response.andReturn().getResponse().getContentAsString(), new TypeReference<List<Message>>() {});

        Assertions.assertNotNull(conversation);
        Assertions.assertEquals(1, conversation.size());
    }
}
