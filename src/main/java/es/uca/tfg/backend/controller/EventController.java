package es.uca.tfg.backend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.uca.tfg.backend.entity.*;
import es.uca.tfg.backend.repository.*;
import es.uca.tfg.backend.rest.EventDTO;
import es.uca.tfg.backend.rest.EventFilterDTO;
import es.uca.tfg.backend.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "http://localhost:8080")
@RequestMapping("/api")
public class EventController {

    @Autowired
    private EventRepository _eventRepository;
    @Autowired
    private EventService _eventService;
    @Autowired
    private UserRepository _userRepository;
    @Autowired
    private InterestRepository _interestRepository;
    @Autowired
    private LocationRepository _locationRepository;
    @Autowired
    private ProvinceRepository _provinceRepository;
    @Autowired
    private RegionRepository _regionRepository;
    @Autowired
    private CountryRepository _countryRepository;
    @Autowired
    private MessageRepository _messageRepository;

    @PostMapping("/newEvent")
    public int newEvent(@RequestBody EventDTO eventDTO) {
        Set<Interest> interests = new HashSet<>();
        System.out.println("EventoDTO: " + eventDTO.get_sTitle());
        User organizer = _userRepository.findBy_iId(eventDTO.get_iOrganizerId());
        if(eventDTO.is_bIsOnline()) {
            Event event = new Event(eventDTO.get_sTitle(), eventDTO.get_tCelebratedAt(), eventDTO.get_tCelebrationHour(), eventDTO.get_sDescription(), organizer,
                    interests, null, null, true);
            event.get_setAssistants().add(organizer);
            event =  _eventRepository.save(event);
            return event.get_iId();
        } else {
            Optional<Location> optionalLocation = _locationRepository.findBy_sName(eventDTO.get_sLocationName());
            Location location;
            for (String sInterest : eventDTO.get_setInterests()) {
                interests.add(_interestRepository.findBy_sName(sInterest));
            }
            if (optionalLocation.isPresent()) {
                location = optionalLocation.get();
            } else {
                System.out.println("Nombre localiz: " + eventDTO.get_sLocationName() + " lat: " + eventDTO.get_dLatitude() + " lon: " + eventDTO.get_dLongitude() + " Prov: " + eventDTO.get_sProvinceName());
                location = _locationRepository.save(new Location(eventDTO.get_sLocationName(), eventDTO.get_dLatitude(), eventDTO.get_dLongitude(), _provinceRepository.findBy_sName(eventDTO.get_sProvinceName())));
            }
            Event event = new Event(eventDTO.get_sTitle(), eventDTO.get_tCelebratedAt(), eventDTO.get_tCelebrationHour(), eventDTO.get_sDescription(), organizer,
                    interests, null, location, false);
            event.get_setAssistants().add(organizer);
            event =  _eventRepository.save(event);
            return event.get_iId();
        }
    }

    @GetMapping("/getEvent/{eventId}")
    public Event getEvent(@PathVariable("eventId") int iEventId) {
        Optional<Event> optionalEvent = _eventRepository.findById(iEventId);
        return optionalEvent.isPresent() ? optionalEvent.get() : new Event();
    }

    @GetMapping("/getEventAssistants/{eventId}")
    public List<User> getEventAssistants(@PathVariable("eventId") int iEventId) {
        Optional<Event> optionalEvent = _eventRepository.findById(iEventId);
        return optionalEvent.isPresent() ? optionalEvent.get().get_setAssistants().stream().toList() : Collections.emptyList();
    }

    @GetMapping("/getEventAssistantIds/{eventId}")
    public List<Integer> getEventAssistantIds(@PathVariable("eventId") int iEventId) {
        Optional<Event> optionalEvent = _eventRepository.findById(iEventId);
        return optionalEvent.isPresent() ? _eventRepository.findAssistantIds(iEventId) : Collections.emptyList();
    }

    @PatchMapping("/setAssist/{eventId}/{userId}")
    public boolean setAssist(@PathVariable("eventId") int iEventId, @PathVariable("userId") int iUserId) {
        Event event = _eventRepository.findById(iEventId).get();
        User user = _userRepository.findBy_iId(iUserId);
        boolean bIsAssisting = false;
        if(event.get_setAssistants().contains(user)) {
            System.out.println("Ya esta el usuario apuntado");
            event.get_setAssistants().remove(user);
        } else {
            System.out.println("El usuario no estaba apuntado");
           bIsAssisting = event.get_setAssistants().add(user);
        }
        event = _eventRepository.save(event);
        return bIsAssisting;
    }

    @GetMapping("/getUserEvents/{userId}")
    public List<Event> getUserEvents(@PathVariable("userId") int iUserId) {
        Optional<User> optionalUser = _userRepository.findById(iUserId);
        return optionalUser.isPresent() ? _eventRepository.findEventsAssistedByUser(optionalUser.get()) : Collections.emptyList();
    }

    @GetMapping("/getFilteredAssistants/{username}/{eventId}")
    public List<User> filterAssistants(@PathVariable("username") String sUsername, @PathVariable("eventId") int iEventId) {
        Optional<Event> optionalEvent = _eventRepository.findById(iEventId);
        if(!optionalEvent.isPresent()) {
            return Collections.emptyList();
        } else {
            //List<User> aFilteredUsers = _userRepository.findBy_sUsernameStartsWith(sUsername);
            //return aFilteredUsers.stream().filter(user ->  optionalEvent.get().get_setAssistants().contains(user)).collect(Collectors.toList());
            return _eventRepository.findFilteredAssistantsByName(sUsername);
        }
    }

    @GetMapping("/getEventInterests/{eventId}")
    public List<Interest> getEventInterests(@PathVariable("eventId") int iEventId) {
        Optional<Event> optionalEvent = _eventRepository.findById(iEventId);
        for(Interest interest: optionalEvent.get().get_setInterest()) {
            System.out.println("Evento con id: " + optionalEvent.get().get_iId() + " tiene interes con nombre: " + interest.get_sName());
        }
        return optionalEvent.isPresent() ? optionalEvent.get().get_setInterest().stream().toList() : Collections.emptyList();
    }

    @PostMapping("/getFilteredEvents/{pageNumber}")
    public Page<Event> getFilteredEvents(@RequestBody EventFilterDTO eventFilterDTO, @PathVariable("pageNumber") int iPageNumber) throws JsonProcessingException {
        int iNumberOfInterests = eventFilterDTO.get_asInterests().size();
        ObjectMapper objectMapper = new ObjectMapper();
        System.out.println(objectMapper.writeValueAsString(eventFilterDTO));
        if(eventFilterDTO.get_bIsOnline()) {
            for(Event event: _eventRepository.findOnlineEvents()) {
                System.out.println(event.get_sTitle());
            }
            return _eventRepository.findOnlineEventIdsByFilter(
                    iNumberOfInterests >= 1 ? _interestRepository.findBy_sName(eventFilterDTO.get_asInterests().get(0)) : null,
                    iNumberOfInterests >= 2 ? _interestRepository.findBy_sName(eventFilterDTO.get_asInterests().get(1)) : null,
                    iNumberOfInterests >= 3 ? _interestRepository.findBy_sName(eventFilterDTO.get_asInterests().get(2)) : null,
                    PageRequest.of(iPageNumber, 5, Sort.by("_tCelebratedAt").ascending()));
        } else {

            for(Event event: _eventRepository.findEventIdsByFilters(_provinceRepository.findBy_sName(eventFilterDTO.get_sProvince()), _regionRepository.findBy_sName(eventFilterDTO.get_sRegion()).get(), _countryRepository.findBy_sName(eventFilterDTO.get_sCountry()).get(),
                    iNumberOfInterests >= 1 ? _interestRepository.findBy_sName(eventFilterDTO.get_asInterests().get(0)) : null,
                    iNumberOfInterests >= 2 ? _interestRepository.findBy_sName(eventFilterDTO.get_asInterests().get(1)) : null,
                    iNumberOfInterests >= 3 ? _interestRepository.findBy_sName(eventFilterDTO.get_asInterests().get(2)) : null,
                    LocalDate.now(), PageRequest.of(iPageNumber, 5, Sort.by("_tCelebratedAt").ascending()))) {
                System.out.println(event.get_sTitle());
                System.out.println("Hoy es antes de la fecha de celebración? " + event.get_tCelebratedAt().isAfter(LocalDate.now()));
            }

            return _eventRepository.findEventIdsByFilters(_provinceRepository.findBy_sName(eventFilterDTO.get_sProvince()), _regionRepository.findBy_sName(eventFilterDTO.get_sRegion()).get(), _countryRepository.findBy_sName(eventFilterDTO.get_sCountry()).get(),
                    iNumberOfInterests >= 1 ? _interestRepository.findBy_sName(eventFilterDTO.get_asInterests().get(0)) : null,
                    iNumberOfInterests >= 2 ? _interestRepository.findBy_sName(eventFilterDTO.get_asInterests().get(1)) : null,
                    iNumberOfInterests >= 3 ? _interestRepository.findBy_sName(eventFilterDTO.get_asInterests().get(2)) : null,
                    LocalDate.now(), PageRequest.of(iPageNumber, 5, Sort.by("_tCelebratedAt").ascending()));
        }
    }

    @PostMapping("/updateEvent/{eventId}")
    public int updateEvent(@RequestBody EventDTO eventDTO, @PathVariable("eventId") int iEventId) {
        Optional<Event> optionalEvent = _eventRepository.findById(iEventId);
        Set<Interest> setInterests = new HashSet<>();
        if(optionalEvent.isPresent()) {
            Event event = optionalEvent.get();
            event.set_sTitle(eventDTO.get_sTitle());
            event.set_sDescription(eventDTO.get_sDescription());
            if(eventDTO.is_bIsOnline()) {
                event.set_bIsOnline(true);
                event.set_location(null);
            } else {
                Optional<Location> optionalLocation = _locationRepository.findBy_sName(eventDTO.get_sLocationName());
                Location location;
                if (optionalLocation.isPresent()) {
                    location = optionalLocation.get();
                } else {
                    location = new Location(eventDTO.get_sLocationName(), eventDTO.get_dLatitude(), eventDTO.get_dLongitude(), _provinceRepository.findBy_sName(eventDTO.get_sProvinceName()));
                    location = _locationRepository.save(location);
                }
                event.set_location(location);
            }
            event.set_tCelebratedAt(eventDTO.get_tCelebratedAt());
            event.set_tCelebrationHour(eventDTO.get_tCelebrationHour());
            for(String sInterest: eventDTO.get_setInterests()) {
                setInterests.add(_interestRepository.findBy_sName(sInterest));
            }
            event.set_setInterests(setInterests);
            event.set_bIsOnline(eventDTO.is_bIsOnline());
            event = _eventRepository.save(event);
            return event.get_iId();
        } else {
            return 0;
        }
    }

    @PatchMapping("softDeleteOrRestoreEvent/{eventId}")
    public boolean softDeleteOrRestoreEvent(@PathVariable("eventId") int iEventId) {
        Optional<Event> optionalEvent = _eventRepository.findById(iEventId);
        if(optionalEvent.isPresent()) {
            if(Objects.equals(optionalEvent.get().get_tDeleteDate(), null))
                optionalEvent.get().set_tDeleteDate(LocalDateTime.now());
            else
                optionalEvent.get().set_tDeleteDate(null);

            System.out.println("Fecha borrado: " + optionalEvent.get().get_tDeleteDate());
            return _eventRepository.save(optionalEvent.get()).get_tDeleteDate() != null;
        } else {
            return false;
        }
    }

    @GetMapping("/countEvents")
    public long countEvents() {
        return _eventRepository.count();
    }
}
