package es.uca.tfg.backend.controller;

import es.uca.tfg.backend.entity.Event;
import es.uca.tfg.backend.entity.Interest;
import es.uca.tfg.backend.entity.Location;
import es.uca.tfg.backend.entity.User;
import es.uca.tfg.backend.repository.EventRepository;
import es.uca.tfg.backend.repository.InterestRepository;
import es.uca.tfg.backend.repository.LocationRepository;
import es.uca.tfg.backend.repository.UserRepository;
import es.uca.tfg.backend.rest.EventDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@CrossOrigin(origins = "http://localhost:8080")
@RequestMapping("/api")
public class EventController {

    @Autowired
    private EventRepository _eventRepository;

    @Autowired
    private UserRepository _userRepository;
    @Autowired
    private InterestRepository _interestRepository;

    @Autowired
    private LocationRepository _locationRepository;

    @PostMapping("/newEvent")
    public void newEvent(@RequestBody EventDTO eventDTO) {
        Set<Interest> interests = new HashSet<>();
        Optional<Location> optionalLocation = _locationRepository.findBy_sName(eventDTO.get_sLocationName());
        User organizer = _userRepository.findBy_iId(eventDTO.get_iOrganizerId());
        Location location;
        for(String sInterest: eventDTO.get_setInterests()) {
            interests.add(_interestRepository.findBy_sName(sInterest));
        }
        if(optionalLocation.isPresent()) {
            location = optionalLocation.get();
        } else {
            location = new Location(eventDTO.get_sLocationName(), eventDTO.get_dLatitude(), eventDTO.get_dLongitude());
            location = _locationRepository.save(location);
        }
        Event event = new Event(eventDTO.get_sTitle(), eventDTO.get_tCelebratedAt(), eventDTO.get_tCelebrationHour(), eventDTO.get_sDescription(), organizer,
                interests, null, location);
        event.get_setAssistants().add(organizer);
        event =  _eventRepository.save(event);
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
            event.get_setAssistants().remove(user);
        } else {
           bIsAssisting = event.get_setAssistants().add(user);
        }
        event = _eventRepository.save(event);
        return bIsAssisting;
    }
}
