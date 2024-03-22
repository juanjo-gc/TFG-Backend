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
        User organizer = _userRepository.findBy_iId(eventDTO.get_iOrganizerId());
        for (String sInterest : eventDTO.get_setInterests()) {
            interests.add(_interestRepository.findBy_sName(sInterest));
        }
        if(eventDTO.is_bIsOnline()) {
            Event event = new Event(eventDTO.get_sTitle(), eventDTO.get_tCelebratedAt(), eventDTO.get_tCelebrationHour(), eventDTO.get_sDescription(), organizer,
                    interests, null, null, true);
            event.get_setAssistants().add(organizer);
            event =  _eventRepository.save(event);
            return event.get_iId();
        } else {
            Optional<Location> optionalLocation = _locationRepository.findBy_sName(eventDTO.get_sLocationName());
            Location location;
            if (optionalLocation.isPresent()) {
                location = optionalLocation.get();
            } else {
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
        Optional<Event> optionalEvent = _eventRepository.findById(iEventId);
        Optional<User> optionalUser = _userRepository.findById(iUserId);
        if(optionalEvent.isPresent() && optionalUser.isPresent()) {
            Event event = optionalEvent.get();
            User user = optionalUser.get();
            boolean bIsAssisting = false;
            if (event.get_setAssistants().contains(user)) {
                event.get_setAssistants().remove(user);
            } else {
                bIsAssisting = event.get_setAssistants().add(user);
            }
            event = _eventRepository.save(event);
            return bIsAssisting;
        } else {
            return false;
        }

    }

    @GetMapping("/getUserEvents/{userId}")
    public List<Event> getUserEvents(@PathVariable("userId") int iUserId) {
        Optional<User> optionalUser = _userRepository.findById(iUserId);
        return optionalUser.isPresent() ? _eventRepository.findEventsAssistedByUser(optionalUser.get()) : Collections.emptyList();
    }

    @GetMapping("/getFilteredAssistants/{username}/{eventId}")
    public List<User> filterAssistants(@PathVariable("username") String sUsername, @PathVariable("eventId") int iEventId) {
        Optional<Event> optionalEvent = _eventRepository.findById(iEventId);
        if(optionalEvent.isPresent()) {
            return _eventRepository.findFilteredAssistantsByName(sUsername);
        } else {
            return Collections.emptyList();
        }
    }

    @GetMapping("/getEventInterests/{eventId}")
    public List<Interest> getEventInterests(@PathVariable("eventId") int iEventId) {
        Optional<Event> optionalEvent = _eventRepository.findById(iEventId);
        for(Interest interest: optionalEvent.get().get_setInterest()) {
        }
        return optionalEvent.isPresent() ? optionalEvent.get().get_setInterest().stream().toList() : Collections.emptyList();
    }

    @PostMapping("/getFilteredEvents/{pageNumber}")
    public Page<Event> getFilteredEvents(@RequestBody EventFilterDTO eventFilterDTO, @PathVariable("pageNumber") int iPageNumber) throws JsonProcessingException {
        int iNumberOfInterests = eventFilterDTO.get_asInterests().size();
        ObjectMapper objectMapper = new ObjectMapper();
        if(eventFilterDTO.get_bIsOnline()) {
            return _eventRepository.findOnlineEventsByFilter(
                    iNumberOfInterests >= 1 ? _interestRepository.findBy_sName(eventFilterDTO.get_asInterests().get(0)) : null,
                    iNumberOfInterests >= 2 ? _interestRepository.findBy_sName(eventFilterDTO.get_asInterests().get(1)) : null,
                    iNumberOfInterests >= 3 ? _interestRepository.findBy_sName(eventFilterDTO.get_asInterests().get(2)) : null,
                    PageRequest.of(iPageNumber, 5, Sort.by("_tCelebratedAt").ascending()));
        } else {
            Optional<Province> optionalProvince = _provinceRepository.findByName(eventFilterDTO.get_sProvince());
            Optional<Region> optionalRegion = _regionRepository.findBy_sName(eventFilterDTO.get_sRegion());
            Optional<Country> optionalCountry = _countryRepository.findBy_sName(eventFilterDTO.get_sCountry());

            return _eventRepository.findEventIdsByFilters(
                    optionalProvince.isPresent() ? optionalProvince.get() : null,
                    optionalRegion.isPresent() ? optionalRegion.get() : null,
                    optionalCountry.isPresent() ? optionalCountry.get() : null,
                    iNumberOfInterests >= 1 ? _interestRepository.findBy_sName(eventFilterDTO.get_asInterests().get(0)) : null,
                    iNumberOfInterests >= 2 ? _interestRepository.findBy_sName(eventFilterDTO.get_asInterests().get(1)) : null,
                    iNumberOfInterests >= 3 ? _interestRepository.findBy_sName(eventFilterDTO.get_asInterests().get(2)) : null,
                    LocalDate.now(), PageRequest.of(iPageNumber, 5, Sort.by("_tCelebratedAt").ascending()));
        }
    }

    @GetMapping("/findHotEvents/{userId}")
    public List<Event> findHotEvents(@PathVariable("userId") int iUserId) {
        Optional<User> optionalUser = _userRepository.findById(iUserId);
        if(optionalUser.isPresent()) {
            if(optionalUser.get().get_province() != null) {
                return _eventRepository.find5LocatedEventsForUser(optionalUser.get().get_province(), optionalUser.get(), PageRequest.of(0, 5)).get().toList();
            } else {
                return _eventRepository.find5OnlineEventsForUser(optionalUser.get(), PageRequest.of(0, 5)).get().toList();
            }
        } else {
            return Collections.emptyList();
        }
    }

    @GetMapping("/getEventAssistantsNumber/{eventId}")
    public int getEventsAssistantsNumber(@PathVariable("eventId") int iEventId) {
        Optional<Event> optionalEvent = _eventRepository.findById(iEventId);
        return optionalEvent.isPresent() ? optionalEvent.get().get_setAssistants().size() : -1;
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
                event.set_bIsOnline(false);
            }
            event.set_tCelebratedAt(eventDTO.get_tCelebratedAt());
            event.set_tCelebrationHour(eventDTO.get_tCelebrationHour());
            for(String sInterest: eventDTO.get_setInterests()) {
                setInterests.add(_interestRepository.findBy_sName(sInterest));
            }
            event.set_setInterests(setInterests);
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
