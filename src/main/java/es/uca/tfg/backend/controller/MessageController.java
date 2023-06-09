package es.uca.tfg.backend.controller;

import es.uca.tfg.backend.entity.Message;
import es.uca.tfg.backend.repository.MessageRepository;
import es.uca.tfg.backend.repository.UserRepository;
import es.uca.tfg.backend.rest.MessageDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:8080")
@RequestMapping("/api")
public class MessageController {
    @Autowired
    MessageRepository _messageRepository;
    @Autowired
    UserRepository _userRepository;
    @PostMapping("/newMessage")
    public Message newMessage(@RequestBody MessageDTO messageDTO) {
        System.out.println("Llega peticion con mensaje: " + messageDTO.get_sText());
        Message message = new Message(messageDTO.get_sText(), _userRepository.findBy_iId(messageDTO.get_iIssuerId()), _userRepository.findBy_iId(messageDTO.get_iRecipientId()));
        return _messageRepository.save(message);
    }
}
