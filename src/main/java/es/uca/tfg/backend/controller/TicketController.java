package es.uca.tfg.backend.controller;

import es.uca.tfg.backend.entity.*;
import es.uca.tfg.backend.repository.*;
import es.uca.tfg.backend.rest.TicketDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:8080")
@RequestMapping("/api")
public class TicketController {

    @Autowired
    private TicketRepository _ticketRepository;
    @Autowired
    private UserRepository _userRepository;
    @Autowired
    private AdminRepository _adminRepository;
    @Autowired
    private EventRepository _eventRepository;
    @Autowired
    private PostRepository _postRepository;
    @Autowired
    private CategoryRepository _categoryRepository;

    @PostMapping("/newTicket")
    public int newTicket(@RequestBody TicketDTO ticketDTO) {
        // String sSubject, String sDescription, int iIssuerId, int iReportedId, int iEventId, int iPostId
        List<Integer> adminIds = _adminRepository.findAllAdminIds();
        Optional<User> optionalIssuer = _userRepository.findById(ticketDTO.get_iIssuerId());
        Optional<Admin> optionalAdmin = _adminRepository.findById(adminIds.get((int)Math.random()*adminIds.size()));
        Optional<Category> optionalCategory = _categoryRepository.findBy_sName(ticketDTO.get_sCategory());
        Ticket ticket = new Ticket();
        System.out.println("Categoria: " + ticketDTO.get_sCategory());
        if(optionalIssuer.isPresent() && optionalAdmin.isPresent()) {
            switch(ticketDTO.get_sCategory()) {
                case "ReportUser":
                    Optional<User> optionalReported = _userRepository.findById(ticketDTO.get_iReportedId());
                    if(optionalReported.isPresent() && optionalCategory.isPresent()) {
                        ticket = _ticketRepository.save(new Ticket(ticketDTO.get_sSubject(), ticketDTO.get_sDescription(), optionalAdmin.get(),
                                optionalIssuer.get(), optionalReported.get(), optionalCategory.get()));
                    }
                    break;
                case "ReportBug":
                    if(optionalCategory.isPresent()) {
                        ticket = _ticketRepository.save(new Ticket(ticketDTO.get_sSubject(), ticketDTO.get_sDescription(), optionalAdmin.get(),
                                optionalIssuer.get(), optionalCategory.get()));
                    }
            }

            return ticket.get_iId();
        } else {
            return 0;
        }
    }

    @GetMapping("/getTicket/{ticketId}")
    public Ticket getTicket(@PathVariable("ticketId") int iTicketId) {
        Optional<Ticket> optionalTicket = _ticketRepository.findById(iTicketId);
        return optionalTicket.isPresent() ? optionalTicket.get() : new Ticket();
    }

    @PatchMapping("openCloseTicket/{ticketId}")
    public boolean openCloseTicket(@PathVariable("ticketId") int iTicketId) {
        Optional<Ticket> optionalTicket = _ticketRepository.findById(iTicketId);
        if(optionalTicket.isPresent()) {
            optionalTicket.get().set_bIsOpen(!optionalTicket.get().is_bIsOpen());
            _ticketRepository.save(optionalTicket.get());
            return optionalTicket.get().is_bIsOpen();
        } else {
            return false;
        }
    }

    @GetMapping("/getUserReports/{userId}")
    public List<Ticket> getUserReports(@PathVariable("userId") int iUserId) {
        Optional<User> optionalUser = _userRepository.findById(iUserId);
        if(optionalUser.isPresent()) {
            return _ticketRepository.findUserReports(optionalUser.get());
        } else {
            return Collections.emptyList();
        }
    }
}
