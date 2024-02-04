package es.uca.tfg.backend.controller;

import es.uca.tfg.backend.config.AbstractTest;
import es.uca.tfg.backend.entity.Event;
import es.uca.tfg.backend.entity.Interest;
import es.uca.tfg.backend.entity.User;
import es.uca.tfg.backend.repository.EventRepository;
import es.uca.tfg.backend.repository.InterestRepository;
import es.uca.tfg.backend.repository.UserRepository;
import es.uca.tfg.backend.rest.InterestDTO;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest
@RunWith(SpringRunner.class)
@ExtendWith(MockitoExtension.class)
public class InterestControllerTestUT extends AbstractTest {

    @Override
    @Before
    public void setUp() {
        super.setUp();
    }

    @Mock
    private InterestRepository _interestRepository;
    @Mock
    private UserRepository _userRepository;
    @Mock
    private EventRepository _eventRepository;

    @InjectMocks
    private InterestController _controller;

    @Mock
    private Interest _interest;

    @Test
    public void updateInterestWillUpdateExistingInterest() {
        //given
        Interest interest = new Interest("Test interest");
        InterestDTO dto = new InterestDTO("Test interest updated");
        Mockito.when(_interestRepository.findById(1)).thenReturn(Optional.of(interest));
        Mockito.when(_interestRepository.save(any(Interest.class))).thenReturn(interest);
        //when
        Interest updated = _controller.updateInterest(dto, 1);
        //then
        Mockito.verify(_interestRepository).save(any(Interest.class));
        Assertions.assertEquals("Test interest updated", updated.get_sName());
    }

    @Test
    public void updateInterestWillFailAsInterestDoesNotExist() {
        //given
        Interest interest = new Interest("Test interest");
        InterestDTO dto = new InterestDTO("Test interest updated");
        Mockito.when(_interestRepository.save(any(Interest.class))).thenReturn(interest);
        //when
        Interest updated = _controller.updateInterest(dto, 1);
        //then
        Mockito.verify(_interestRepository, Mockito.times(0)).save(any(Interest.class));
        Assertions.assertEquals(0, updated.get_iId());
    }

    @Test
    public void deleteInterestWillDeleteExistingInterest() {
        //given
        Event event = Mockito.mock(Event.class);
        Set<User> aUsersWithInterest = Set.of(new User(), new User(), new User());
        Set<Interest> aEventInterests = Set.of(new Interest(), new Interest());
        List<Event> aEventsWithInterest = List.of(event, event);
        Mockito.when(_interestRepository.findById(1)).thenReturn(Optional.of(_interest));
        Mockito.when(_interest.get_setUsers()).thenReturn(aUsersWithInterest);
        Mockito.when(event.get_setInterests()).thenReturn(aEventInterests);
        Mockito.when(_eventRepository.findByInterest(any(Interest.class))).thenReturn(aEventsWithInterest);
        //when
        _controller.deleteInterest(1);
        //then
        Mockito.verify(_userRepository, Mockito.times(3)).save(any(User.class));
        Mockito.verify(_eventRepository, Mockito.times(2)).save(any(Event.class));
        Mockito.verify(_interestRepository).deleteById(1);
    }

    @Test
    public void deleteInterestWillFailAsInterestDoesNotExist() {
        //given
        Event event = Mockito.mock(Event.class);
        Set<User> aUsersWithInterest = Set.of(new User(), new User(), new User());
        Set<Interest> aEventInterests = Set.of(new Interest(), new Interest());
        List<Event> aEventsWithInterest = List.of(event, event);
        Mockito.when(_interest.get_setUsers()).thenReturn(aUsersWithInterest);
        Mockito.when(event.get_setInterests()).thenReturn(aEventInterests);
        Mockito.when(_eventRepository.findByInterest(any(Interest.class))).thenReturn(aEventsWithInterest);
        //when
        _controller.deleteInterest(1);
        //then
        Mockito.verify(_userRepository, Mockito.times(0)).save(any(User.class));
        Mockito.verify(_eventRepository, Mockito.times(0)).save(any(Event.class));
        Mockito.verify(_interestRepository, Mockito.times(0)).deleteById(1);
    }

}
