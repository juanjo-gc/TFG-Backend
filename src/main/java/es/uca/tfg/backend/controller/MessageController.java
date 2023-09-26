package es.uca.tfg.backend.controller;

import com.pusher.rest.Pusher;
import es.uca.tfg.backend.entity.Message;
import es.uca.tfg.backend.entity.User;
import es.uca.tfg.backend.repository.MessageRepository;
import es.uca.tfg.backend.repository.UserRepository;
import es.uca.tfg.backend.rest.MessageDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "http://localhost:8080")
@RequestMapping("/api")
public class MessageController {

    @Autowired
    private MessageRepository _messageRepository;
    @Autowired
    private UserRepository _userRepository;

    private Pusher pusher;

    public MessageController() {
        pusher = new Pusher("1618298", "56c83b0b9e4a25060b44", "b08e67dde55ec35c6427");
        pusher.setCluster("eu");
        pusher.setEncrypted(true);
    }

    @PostMapping("/newMessage")
    public Message newMessage(@RequestBody MessageDTO messageDTO) {
        System.out.println("Llega peticion con mensaje: " + messageDTO.get_sText());
        Message message = new Message(messageDTO.get_sText(), _userRepository.findBy_iId(messageDTO.get_iIssuerId()), _userRepository.findBy_iId(messageDTO.get_iRecipientId()));
        pusher.trigger("rt-chat", "newMessage", Collections.singletonMap("message", messageDTO));
        return _messageRepository.save(message);
    }

    @GetMapping("/getLastMessages/{userId}")
    public List<Message> getLastMessages(@PathVariable("userId") int iUserId) {
        Optional<User> optionalUser = _userRepository.findById(iUserId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            List<User> aUsersWithActiveChat = _messageRepository.findMessagedUsers(user);
            aUsersWithActiveChat.addAll(_messageRepository.findUserWhoMessagedCurrentUser(user));
            List<Message> aLastMessages = new ArrayList<>();

            for (User messagedUser : aUsersWithActiveChat.stream().distinct().collect(Collectors.toList())) {
                Page<Message> pageMessage = _messageRepository.findLastIssuerRecipientMessage(user, messagedUser, PageRequest.of(0, 1));
                Message message = pageMessage.get().findFirst().get();
                aLastMessages.add(message);
            }

            return aLastMessages;
        } else {
            return Collections.emptyList();
        }
    }
    /*
    @GetMapping("/getMessagedUsers")
    public List<User> messagedUsers() {
        return _messageRepository.findMessagedUsers(_userRepository.findBy_iId(2));
    }
     */

    @GetMapping("/getConversation/{userId}/{targetId}")
    public List<Message> getConversation(@PathVariable("userId") int iUserId, @PathVariable("targetId") int iTargetId) {
        Optional<User> optionalUser = _userRepository.findById(iUserId);
        Optional<User> optionalTargetUser = _userRepository.findById(iTargetId);

        if (optionalUser.isPresent() && optionalTargetUser.isPresent()) {
            return _messageRepository.findConversation(optionalUser.get(), optionalTargetUser.get());
        } else {
            return Collections.emptyList();
        }
    }

    @PatchMapping("/setSeenMessages/{userId}/{targetId}")
    public void setSeenMessages(@PathVariable("userId") int iUserId, @PathVariable("targetId") int iTargetId) {

        Optional<User> optionalUser = _userRepository.findById(iUserId);
        Optional<User> optionalTargetUser = _userRepository.findById(iTargetId);

        if (optionalUser.isPresent() && optionalTargetUser.isPresent()) {
            for (Message message : _messageRepository.findNotSeenMessages(optionalUser.get(), optionalTargetUser.get())) {
                message.set_bSeen(true);
                _messageRepository.save(message);
            }
        }
    }

    @GetMapping("/countMessages")
    public long countMessages() {
        return _messageRepository.count();
    }
}
