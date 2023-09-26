package es.uca.tfg.backend.controller;

import es.uca.tfg.backend.entity.Person;
import es.uca.tfg.backend.entity.Reply;
import es.uca.tfg.backend.entity.Ticket;
import es.uca.tfg.backend.repository.*;
import es.uca.tfg.backend.rest.ReplyDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:8080")
@RequestMapping("/api")
public class ReplyController {

    @Autowired
    private ReplyRepository _replyRepository;
    @Autowired
    private TicketRepository _ticketRepository;
    @Autowired
    private UserRepository _userRepository;
    @Autowired
    private AdminRepository _adminRepository;
    @Autowired
    private PersonRepository _personRepository;

    @PostMapping("/newReply")
    public Reply newReply(@RequestBody ReplyDTO replyDTO) {
        Optional<Ticket> optionalTicket = _ticketRepository.findById(replyDTO.get_iTicketId());
        Optional<Person> optionalPerson = _personRepository.findById(replyDTO.get_iPersonId());
        if(optionalPerson.isPresent() && optionalTicket.isPresent()) {
            return _replyRepository.save(new Reply(replyDTO.get_sText(), optionalPerson.get(), optionalTicket.get()));
        } else {
            return new Reply();
        }
    }

    @GetMapping("/getTicketReplies/{ticketId}")
    public List<Reply> getTicketReplies(@PathVariable("ticketId") int iTicketId) {
        Optional<Ticket> optionalTicket = _ticketRepository.findById(iTicketId);
        return optionalTicket.isPresent() ? _replyRepository.findByTicketOrderByCreatedAtAsc(optionalTicket.get()) : Collections.emptyList();
    }
}
