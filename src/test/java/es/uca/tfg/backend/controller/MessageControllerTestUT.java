package es.uca.tfg.backend.controller;
import es.uca.tfg.backend.config.AbstractTest;
import es.uca.tfg.backend.entity.Message;
import es.uca.tfg.backend.entity.User;
import es.uca.tfg.backend.repository.MessageRepository;
import es.uca.tfg.backend.repository.UserRepository;
import es.uca.tfg.backend.rest.MessageDTO;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.*;

@WebMvcTest
@RunWith(SpringRunner.class)
@ExtendWith(MockitoExtension.class)
public class MessageControllerTestUT extends AbstractTest {

    @Override
    @Before
    public void setUp() {
        super.setUp();
    }

    @Mock
    private MessageRepository _messageRepository;
    @Mock
    private UserRepository _userRepository;

    @InjectMocks
    private MessageController _controller;

    @Mock
    private User _user;

    @Test
    public void newMessageWillCreateMessage() {
        //given
        MessageDTO dto = new MessageDTO(1, 2, "Test message");
        Mockito.when(_messageRepository.save(any(Message.class))).thenReturn(new Message());
        //when
        Message message = _controller.newMessage(dto);
        //then
        Mockito.verify(_messageRepository).save(any(Message.class));
    }

    @Test
    public void getLastMessagesWillReturnMessageList() {
        //given
        Page<Message> lastMessage = Mockito.mock(Page.class);
        Stream<Message> auxStream = Mockito.mock(Stream.class);
        List<User> aMessagedUsers = new ArrayList<>();
        for(int i = 0; i < 5; i++)
            aMessagedUsers.add(new User());
        Mockito.when(_userRepository.findById(1)).thenReturn(Optional.of(_user));
        Mockito.when(_messageRepository.findMessagedUsers(_user)).thenReturn(aMessagedUsers);
        Mockito.when(_messageRepository.findUserWhoMessagedCurrentUser(_user)).thenReturn(aMessagedUsers);
        Mockito.when(_messageRepository.findLastIssuerRecipientMessage(any(User.class), any(User.class), any(Pageable.class))).thenReturn(lastMessage);
        Mockito.when(lastMessage.get()).thenReturn(auxStream);
        Mockito.when(auxStream.findFirst()).thenReturn(Optional.of(new Message()));
        //when
        List<Message> aLastMessages = _controller.getLastMessages(1);
        //then
        Mockito.verify(_messageRepository).findMessagedUsers(_user);
        Mockito.verify(_messageRepository).findUserWhoMessagedCurrentUser(_user);
        Mockito.verify(_messageRepository, Mockito.times(5)).findLastIssuerRecipientMessage(any(User.class), any(User.class), any(Pageable.class));
        Assertions.assertEquals(5, aLastMessages.size());
    }

    @Test
    public void getLastMessagesWillFailAndReturnEmptyListAsUserDoesNotExist() {
        //given
        Page<Message> lastMessage = Mockito.mock(Page.class);
        Stream<Message> auxStream = Mockito.mock(Stream.class);
        List<User> aMessagedUsers = new ArrayList<>();
        for(int i = 0; i < 5; i++)
            aMessagedUsers.add(new User());
        Mockito.when(_messageRepository.findMessagedUsers(_user)).thenReturn(aMessagedUsers);
        Mockito.when(_messageRepository.findUserWhoMessagedCurrentUser(_user)).thenReturn(aMessagedUsers);
        Mockito.when(_messageRepository.findLastIssuerRecipientMessage(any(User.class), any(User.class), any(Pageable.class))).thenReturn(lastMessage);
        Mockito.when(lastMessage.get()).thenReturn(auxStream);
        Mockito.when(auxStream.findFirst()).thenReturn(Optional.of(new Message()));
        //when
        List<Message> aLastMessages = _controller.getLastMessages(1);
        //then
        Mockito.verify(_messageRepository, Mockito.times(0)).findMessagedUsers(_user);
        Mockito.verify(_messageRepository, Mockito.times(0)).findUserWhoMessagedCurrentUser(_user);
        Mockito.verify(_messageRepository, Mockito.times(0)).findLastIssuerRecipientMessage(any(User.class), any(User.class), any(Pageable.class));
        Assertions.assertEquals(0, aLastMessages.size());
    }

    @Test
    public void getConversationWillReturnConversationBewteenUsers() {
        //given
        User recipient = Mockito.mock(User.class);
        List<Message> aMessages = new ArrayList<>();
        for(int i =0; i < 5; i++)
            aMessages.add(new Message());
        Mockito.when(_userRepository.findById(1)).thenReturn(Optional.of(_user));
        Mockito.when(_userRepository.findById(2)).thenReturn(Optional.of(recipient));
        Mockito.when(_messageRepository.findConversation(_user, recipient)).thenReturn(aMessages);
        //when
        List<Message> aConversation = _controller.getConversation(1, 2);
        //then
        Mockito.verify(_messageRepository).findConversation(_user, recipient);
        Assertions.assertEquals(5, aConversation.size());
    }

    @Test
    public void getConversationWillFailAndReturnEmptyListAsOneUserAtLeastDoesNotExist() {
        //given
        User recipient = Mockito.mock(User.class);
        List<Message> aMessages = new ArrayList<>();
        for(int i =0; i < 5; i++)
            aMessages.add(new Message());
        Mockito.when(_userRepository.findById(2)).thenReturn(Optional.of(recipient));
        Mockito.when(_messageRepository.findConversation(_user, recipient)).thenReturn(aMessages);
        //when
        List<Message> aConversation = _controller.getConversation(1, 2);
        //then
        Mockito.verify(_messageRepository, Mockito.times(0)).findConversation(_user, recipient);
        Assertions.assertEquals(0, aConversation.size());
    }

    @Test
    public void setSeenMessagesWillMarkMessagesAsSeen(){
        //given
        User recipient = Mockito.mock(User.class);
        List<Message> aMessages = new ArrayList<>();
        for(int i =0; i < 5; i++) {
            Message message = new Message();
            message.set_bSeen(false);
            aMessages.add(message);
        }
        Mockito.when(_userRepository.findById(1)).thenReturn(Optional.of(_user));
        Mockito.when(_userRepository.findById(2)).thenReturn(Optional.of(recipient));
        Mockito.when(_messageRepository.findNotSeenMessages(_user, recipient)).thenReturn(aMessages);
        //when
        _controller.setSeenMessages(1, 2);
        //then
        Mockito.verify(_messageRepository, Mockito.times(5)).save(any(Message.class));
        Mockito.verify(_messageRepository).findNotSeenMessages(_user, recipient);
        for(Message message: aMessages)
            Assertions.assertTrue(message.is_bSeen());
    }

    @Test
    public void setSeenMessagesWillFailAsAtLeastOneUserDoesNotExist(){
        //given
        User recipient = Mockito.mock(User.class);
        List<Message> aMessages = new ArrayList<>();
        for(int i =0; i < 5; i++) {
            Message message = new Message();
            message.set_bSeen(false);
            aMessages.add(message);
        }
        Mockito.when(_userRepository.findById(2)).thenReturn(Optional.of(recipient));
        Mockito.when(_messageRepository.findNotSeenMessages(_user, recipient)).thenReturn(aMessages);
        //when
        _controller.setSeenMessages(1, 2);
        //then
        Mockito.verify(_messageRepository, Mockito.times(0)).save(any(Message.class));
        Mockito.verify(_messageRepository, Mockito.times(0)).findNotSeenMessages(_user, recipient);
    }
}
