package es.uca.tfg.backend.controller;

import es.uca.tfg.backend.entity.*;
import es.uca.tfg.backend.repository.*;
import es.uca.tfg.backend.rest.TicketDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
        System.out.println("La comprobacion funciona? " + ticketDTO.get_sCategory().equals("Denunciar un usuario"));
        if(optionalIssuer.isPresent() && optionalAdmin.isPresent()) {
            System.out.println("Entra en if. Lo siguiente es el switch");
            switch(ticketDTO.get_sCategory()) {
                case "Denunciar un usuario":
                    Optional<User> optionalReported = _userRepository.findById(ticketDTO.get_iReportedId());
                    System.out.println(optionalReported.get().get_iId());
                    if(optionalReported.isPresent() && optionalCategory.isPresent()) {
                        ticket = _ticketRepository.save(new Ticket(ticketDTO.get_sSubject(), ticketDTO.get_sDescription(), optionalAdmin.get(),
                                optionalIssuer.get(), optionalReported.get(), optionalCategory.get()));
                        System.out.println("Se ha creado la denuncia");
                    }
                    break;
                case "Reportar un error":
                    if(optionalCategory.isPresent()) {
                        ticket = _ticketRepository.save(new Ticket(ticketDTO.get_sSubject(), ticketDTO.get_sDescription(), optionalAdmin.get(),
                                optionalIssuer.get(), optionalCategory.get()));
                    }
                    break;
                case "Denunciar una publicación":
                    Optional<Post> optionalPost = _postRepository.findById(ticketDTO.get_iPostId());
                    System.out.println(ticketDTO.get_sDescription());
                    if(optionalPost.isPresent() && optionalCategory.isPresent()) {
                        ticket = _ticketRepository.save(new Ticket(ticketDTO.get_sSubject(), ticketDTO.get_sDescription(), optionalAdmin.get(), optionalIssuer.get(),
                                null, null, optionalPost.get(), optionalCategory.get()));
                    }
                    break;
                case "Denunciar un evento":
                    Optional<Event> optionalEvent = _eventRepository.findById(ticketDTO.get_iEventId());
                    if(optionalEvent.isPresent() && optionalCategory.isPresent()) {
                        ticket = _ticketRepository.save(new Ticket(ticketDTO.get_sSubject(), ticketDTO.get_sDescription(), optionalAdmin.get(), optionalIssuer.get(), null,
                                optionalEvent.get(), null, optionalCategory.get()));
                    }
                    break;
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

    @GetMapping("/getAdminTickets/{adminId}")
    public List<Ticket> getAdminTickets(@PathVariable("adminId") int iAdminId) {
        Optional<Admin> optionalAdmin = _adminRepository.findById(iAdminId);
        if (optionalAdmin.isPresent()) {
            return _ticketRepository.findBy_admin(optionalAdmin.get());
        } else {
            return Collections.emptyList();
        }
    }

    @GetMapping("/getUserTickets/{userId}")
    public List<Ticket> getUserTickets(@PathVariable("userId") int iUserId) {
        Optional<User> optionalUser = _userRepository.findById(iUserId);
        return optionalUser.isPresent() ? _ticketRepository.findBy_issuer(optionalUser.get()) : Collections.emptyList();
    }
}
