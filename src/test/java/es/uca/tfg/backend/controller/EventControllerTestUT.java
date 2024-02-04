package es.uca.tfg.backend.controller;
import es.uca.tfg.backend.config.AbstractTest;
import es.uca.tfg.backend.entity.*;
import es.uca.tfg.backend.repository.*;
import es.uca.tfg.backend.rest.EventDTO;
import es.uca.tfg.backend.service.EventService;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Stream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest
@RunWith(SpringRunner.class)
@ExtendWith(MockitoExtension.class)
public class EventControllerTestUT extends AbstractTest {
    
    @Override
    @Before
    public void setUp() {
        super.setUp();
    }

    @Mock
    private EventRepository _eventRepository;
    @Mock
    private EventService _eventService;
    @Mock
    private UserRepository _userRepository;
    @Mock
    private InterestRepository _interestRepository;
    @Mock
    private LocationRepository _locationRepository;
    @Mock
    private ProvinceRepository _provinceRepository;
    @Mock
    private RegionRepository _regionRepository;
    @Mock
    private CountryRepository _countryRepository;
    @Mock
    private MessageRepository _messageRepository;

    @InjectMocks
    private EventController _controller;

    @Mock
    private User _user;

    @Mock
    private Location _location;

    @Mock
    private Event _event;

    @Test
    public void newEventWillCreateNewLocatedEventInRegisteredLocation() {
        //given
        EventDTO dto = new EventDTO("Test title", LocalDate.now(), LocalTime.now(), "Test description", 1, Set.of("i1", "i2"), "Test location",
        1.45f, 1.57f, "Cádiz", false);
        Mockito.when(_userRepository.findById(1)).thenReturn(Optional.of(_user));
        Mockito.when(_locationRepository.findBy_sName(anyString())).thenReturn(Optional.of(_location));
        Mockito.when(_eventRepository.save(any(Event.class))).thenReturn(_event);
        Mockito.when(_event.get_iId()).thenReturn(10);
        //when
        int iEventId = _controller.newEvent(dto);
        //then
        Mockito.verify(_eventRepository, Mockito.times(1)).save(any(Event.class));
        Mockito.verify(_locationRepository, Mockito.times(0)).save(any(Location.class));
        Mockito.verify(_interestRepository, Mockito.times(1)).findBy_sName("i1");
        Mockito.verify(_interestRepository, Mockito.times(1)).findBy_sName("i2");
        Assertions.assertEquals(10, iEventId);
    }

    @Test
    public void newEventWillCreateNewLocatedEventInNonRegisteredLocation() {
        //given
        EventDTO dto = new EventDTO("Test title", LocalDate.now(), LocalTime.now(), "Test description", 1, Set.of("i1", "i2"), "Test location",
                1.45f, 1.57f, "Cádiz", false);
        Mockito.when(_userRepository.findById(1)).thenReturn(Optional.of(_user));
        Mockito.when(_eventRepository.save(any(Event.class))).thenReturn(_event);
        Mockito.when(_event.get_iId()).thenReturn(10);
        //when
        int iEventId = _controller.newEvent(dto);
        //then
        Mockito.verify(_eventRepository, Mockito.times(1)).save(any(Event.class));
        Mockito.verify(_interestRepository, Mockito.times(1)).findBy_sName("i1");
        Mockito.verify(_interestRepository, Mockito.times(1)).findBy_sName("i2");
        Mockito.verify(_locationRepository, Mockito.times(1)).save(any(Location.class));
        Assertions.assertEquals(10, iEventId);
    }

    @Test
    public void newEventWillCreateNewOnlineEvent() {
        //given
        EventDTO dto = new EventDTO("Test title", LocalDate.now(), LocalTime.now(), "Test description", 1, Set.of("i1", "i2"), "",
                0f, 0f, "", true);
        Mockito.when(_userRepository.findById(1)).thenReturn(Optional.of(_user));
        Mockito.when(_eventRepository.save(any(Event.class))).thenReturn(_event);
        Mockito.when(_event.get_iId()).thenReturn(10);
        //when
        int iEventId = _controller.newEvent(dto);
        //then
        Mockito.verify(_eventRepository, Mockito.times(1)).save(any(Event.class));
        Mockito.verify(_locationRepository, Mockito.times(0)).save(any(Location.class));
        Mockito.verify(_interestRepository, Mockito.times(1)).findBy_sName("i1");
        Mockito.verify(_interestRepository, Mockito.times(1)).findBy_sName("i2");
        Assertions.assertEquals(10, iEventId);
    }

    @Test
    public void getEventWillReturnExistingEvent() {
        //given
        Mockito.when(_eventRepository.findById(1)).thenReturn(Optional.of(_event));
        Mockito.when(_event.get_iId()).thenReturn(10);
        //when
        Event event = _controller.getEvent(1);
        //then
        Assertions.assertEquals(10, event.get_iId());
    }

    @Test
    public void getEventWillFailAsEventDoesNotExist() {
        //given
        Mockito.when(_eventRepository.findById(1)).thenReturn(Optional.of(_event));
        //when
        Event event = _controller.getEvent(1);
        //then
        Assertions.assertEquals(0, event.get_iId());
    }

    //Las pruebas de los métodos análogos a este serán suprimidas, ya que los aspectos a tener en cuenta se probarán en otras clases de prueba

    @Test
    public void setAssistWillAddUserToAssistantsSet() {
        //given
        Set<User> aAssistants = Mockito.mock(Set.class);
        Mockito.when(_eventRepository.findById(1)).thenReturn(Optional.of(_event));
        Mockito.when(_userRepository.findById(1)).thenReturn(Optional.of(_user));
        Mockito.when(_event.get_setAssistants()).thenReturn(aAssistants);
        Mockito.when(aAssistants.contains(_user)).thenReturn(false);
        //when
        boolean bIsAssisting = _controller.setAssist(1, 1);
        //then
        Mockito.verify(aAssistants).contains(_user);
        Mockito.verify(aAssistants).add(_user);
        Mockito.verify(aAssistants, Mockito.times(0)).remove(_user);
    }

    @Test
    public void setAssistWillRemoveUserFromAssistantsSet() {
        //given
        Set<User> aAssistants = Mockito.mock(Set.class);
        Mockito.when(_eventRepository.findById(1)).thenReturn(Optional.of(_event));
        Mockito.when(_userRepository.findById(1)).thenReturn(Optional.of(_user));
        Mockito.when(_event.get_setAssistants()).thenReturn(aAssistants);
        Mockito.when(aAssistants.contains(_user)).thenReturn(true);
        //when
        boolean bIsAssisting = _controller.setAssist(1, 1);
        //then
        Mockito.verify(aAssistants).contains(_user);
        Mockito.verify(aAssistants).remove(_user);
        Mockito.verify(aAssistants, Mockito.times(0)).add(_user);
    }

    @Test
    public void setAssistWillFailAsEventOrUserDoesNotExist() {
        //given
        Set<User> aAssistants = Mockito.mock(Set.class);
        Mockito.when(_event.get_setAssistants()).thenReturn(aAssistants);
        Mockito.when(aAssistants.contains(_user)).thenReturn(true);
        //when
        boolean bIsAssisting = _controller.setAssist(1, 1);
        //then
        Mockito.verify(aAssistants, Mockito.times(0)).contains(_user);
        Mockito.verify(aAssistants, Mockito.times(0)).remove(_user);
        Mockito.verify(aAssistants, Mockito.times(0)).add(_user);
    }

    @Test
    public void findHotEventsWillReturnEventsList() {
        //given
        List<Event> aHotEvents = new ArrayList<>();
        List<Event> aHotOnlineEvents = new ArrayList<>();
        Page<Event> aHotEventsPage = Mockito.mock(Page.class);
        Page<Event> aHotOnlineEventsPage = Mockito.mock(Page.class);
        Stream<Event> auxStream = Mockito.mock(Stream.class);
        for(int i = 0; i < 5; i++) {
            aHotEvents.add(new Event());
            if(i != 4)
                aHotOnlineEvents.add(new Event());
        }
        Mockito.when(_user.get_province()).thenReturn(new Province());
        Mockito.when(_userRepository.findById(1)).thenReturn(Optional.of(_user));
        Mockito.when(_eventRepository.find5LocatedEventsForUser(any(Province.class), any(User.class), any(Pageable.class))).thenReturn(aHotEventsPage);
        Mockito.when(_eventRepository.find5OnlineEventsForUser(_user, PageRequest.of(0, 5))).thenReturn(aHotOnlineEventsPage);
        Mockito.when(aHotEventsPage.get()).thenReturn(auxStream);
        Mockito.when(auxStream.toList()).thenReturn(aHotEvents);
        //when
        List<Event> aEvents = _controller.findHotEvents(1);
        //then
        Mockito.verify(_eventRepository).find5LocatedEventsForUser(any(Province.class), any(User.class), any(Pageable.class));
        Mockito.verify(_eventRepository, Mockito.times(0)).find5OnlineEventsForUser(_user, PageRequest.of(0, 5));
    }

    @Test
    public void findHotOnlineEventsWillReturnEventsList() {
        //given
        List<Event> aHotEvents = new ArrayList<>();
        List<Event> aHotOnlineEvents = new ArrayList<>();
        Page<Event> aHotEventsPage = Mockito.mock(Page.class);
        Page<Event> aHotOnlineEventsPage = Mockito.mock(Page.class);
        Stream<Event> auxStream = Mockito.mock(Stream.class);
        for(int i = 0; i < 5; i++) {
            aHotEvents.add(new Event());
            if(i != 4)
                aHotOnlineEvents.add(new Event());
        }
        Mockito.when(_user.get_province()).thenReturn(null);
        Mockito.when(_userRepository.findById(1)).thenReturn(Optional.of(_user));
        Mockito.when(_eventRepository.find5LocatedEventsForUser(any(Province.class), any(User.class), any(Pageable.class))).thenReturn(aHotEventsPage);
        Mockito.when(_eventRepository.find5OnlineEventsForUser(_user, PageRequest.of(0, 5))).thenReturn(aHotOnlineEventsPage);
        Mockito.when(aHotEventsPage.get()).thenReturn(auxStream);
        Mockito.when(auxStream.toList()).thenReturn(aHotOnlineEvents);
        //when
        List<Event> aEvents = _controller.findHotEvents(1);
        //then
        Mockito.verify(_eventRepository, Mockito.times(0)).find5LocatedEventsForUser(any(Province.class), any(User.class), any(Pageable.class));
        Mockito.verify(_eventRepository).find5OnlineEventsForUser(_user, PageRequest.of(0, 5));
    }

    @Test
    public void updateEventWillUpdateExistingOnlineEvent() {
        //given
        EventDTO dto = new EventDTO("Test title", LocalDate.now(), LocalTime.now(), "Test description", 1, Set.of("i1", "i2"), "",
                0f, 0f, "", true);
        Mockito.when(_eventRepository.findById(10)).thenReturn(Optional.of(_event));
        Mockito.when(_interestRepository.findBy_sName("i1")).thenReturn(new Interest("i1"));
        Mockito.when(_interestRepository.findBy_sName("i2")).thenReturn(new Interest("i2"));
        Mockito.when(_eventRepository.save(_event)).thenReturn(_event);
        Mockito.when(_event.get_iId()).thenReturn(10);
        //when
        int iEventId = _controller.updateEvent(dto, 10);
        //then
        Mockito.verify(_event).set_sTitle(anyString());
        Mockito.verify(_event).set_sDescription(anyString());
        Mockito.verify(_event).set_bIsOnline(true);
        Mockito.verify(_event).set_location(null);
        Mockito.verify(_event).set_tCelebratedAt(any(LocalDate.class));
        Mockito.verify(_event).set_tCelebrationHour(any(LocalTime.class));
        Mockito.verify(_event).set_setInterests(any(Set.class));
        Mockito.verify(_eventRepository).save(_event);
        Assertions.assertEquals(10, iEventId);
    }

    @Test
    public void updateEventWillUpdateExistingLocatedEventInRegisteredLocation() {
        //given
        EventDTO dto = new EventDTO("Test title", LocalDate.now(), LocalTime.now(), "Test description", 1, Set.of("i1", "i2"), "Test location",
                1.5f, 1.5f, "Cádiz", false);
        Mockito.when(_eventRepository.findById(10)).thenReturn(Optional.of(_event));
        Mockito.when(_locationRepository.findBy_sName(anyString())).thenReturn(Optional.of(_location));
        Mockito.when(_interestRepository.findBy_sName("i1")).thenReturn(new Interest("i1"));
        Mockito.when(_interestRepository.findBy_sName("i2")).thenReturn(new Interest("i2"));
        Mockito.when(_eventRepository.save(_event)).thenReturn(_event);
        Mockito.when(_event.get_iId()).thenReturn(10);
        //when
        int iEventId = _controller.updateEvent(dto, 10);
        //then
        Mockito.verify(_event).set_sTitle(anyString());
        Mockito.verify(_event).set_sDescription(anyString());
        Mockito.verify(_event).set_bIsOnline(false);
        Mockito.verify(_event).set_location(_location);
        Mockito.verify(_event).set_tCelebratedAt(any(LocalDate.class));
        Mockito.verify(_event).set_tCelebrationHour(any(LocalTime.class));
        Mockito.verify(_event).set_setInterests(any(Set.class));
        Mockito.verify(_locationRepository, Mockito.times(0)).save(_location);
        Mockito.verify(_eventRepository).save(_event);
        Assertions.assertEquals(10, iEventId);
    }

    @Test
    public void updateEventWillUpdateExistingLocatedEventInUnregisteredLocation() {
        //given
        EventDTO dto = new EventDTO("Test title", LocalDate.now(), LocalTime.now(), "Test description", 1, Set.of("i1", "i2"), "Test location",
                1.5f, 1.5f, "Cádiz", false);
        Mockito.when(_eventRepository.findById(10)).thenReturn(Optional.of(_event));
        Mockito.when(_interestRepository.findBy_sName("i1")).thenReturn(new Interest("i1"));
        Mockito.when(_interestRepository.findBy_sName("i2")).thenReturn(new Interest("i2"));
        Mockito.when(_eventRepository.save(_event)).thenReturn(_event);
        Mockito.when(_locationRepository.save(any(Location.class))).thenReturn(_location);
        Mockito.when(_event.get_iId()).thenReturn(10);
        //when
        int iEventId = _controller.updateEvent(dto, 10);
        //then
        Mockito.verify(_event).set_sTitle(anyString());
        Mockito.verify(_event).set_sDescription(anyString());
        Mockito.verify(_event).set_bIsOnline(false);
        Mockito.verify(_event).set_location(_location);
        Mockito.verify(_event).set_tCelebratedAt(any(LocalDate.class));
        Mockito.verify(_event).set_tCelebrationHour(any(LocalTime.class));
        Mockito.verify(_event).set_setInterests(any(Set.class));
        Mockito.verify(_locationRepository).save(any(Location.class));
        Mockito.verify(_eventRepository).save(_event);
        Assertions.assertEquals(10, iEventId);
    }

    @Test
    public void softDeleteOrRestoreEventWillDeleteExistingEvent() {
        //given
        Mockito.when(_eventRepository.findById(10)).thenReturn(Optional.of(_event));
        Mockito.when(_event.get_tDeleteDate()).thenReturn(null);
        Mockito.when(_eventRepository.save(any(Event.class))).thenReturn(_event);
        //when
        boolean bHasBeenDeleted = _controller.softDeleteOrRestoreEvent(10);
        //then
        Mockito.verify(_event).set_tDeleteDate(any(LocalDateTime.class));
    }

    @Test
    public void softDeleteOrRestoreEventWillRestoreDeletedEvent() {
        //given
        Mockito.when(_eventRepository.findById(10)).thenReturn(Optional.of(_event));
        Mockito.when(_event.get_tDeleteDate()).thenReturn(LocalDateTime.now());
        Mockito.when(_eventRepository.save(any(Event.class))).thenReturn(_event);
        //when
        boolean bHasBeenDeleted = _controller.softDeleteOrRestoreEvent(10);
        //then
        Mockito.verify(_event).set_tDeleteDate(null);
    }
}
