package es.uca.tfg.backend.integration.controller;
import es.uca.tfg.backend.config.AbstractTest;
import es.uca.tfg.backend.entity.Event;
import es.uca.tfg.backend.entity.Interest;
import es.uca.tfg.backend.entity.User;
import es.uca.tfg.backend.rest.InterestDTO;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.*;

@WebMvcTest
@RunWith(SpringRunner.class)
public class InterestControllerTestIT extends AbstractTest {

    @Override
    @Before
    public void setUp() {
        super.setUp();
    }

    @Test
    public void updateInterestWillUpdateExistingInterest() throws Exception {
        //given
        Interest interest = new Interest("InterestTest");
        InterestDTO interestDTO = new InterestDTO("UpdatedInterest");
        Mockito.when(_interestRepository.findById(1)).thenReturn(Optional.of(interest));
        Mockito.when(_interestRepository.save(any(Interest.class))).thenReturn(interest);
        //when
        ResultActions resultActions = _mockMvc.perform(patch("/api/updateInterest/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(_objectMapper.writeValueAsString(interestDTO)));
        //then
        resultActions.andDo(print())
                .andExpect(status().is2xxSuccessful());
        Assertions.assertEquals("UpdatedInterest", _objectMapper.readValue(resultActions.andReturn().getResponse().getContentAsString(), Interest.class).get_sName());
        Mockito.verify(_interestRepository).save(any(Interest.class));
    }

    @Test
    public void deleteInterestWillDeleteExistingInterest() throws Exception {
        //given
        Interest interest = Mockito.mock(Interest.class);
        List<Event> eventsWithInterest = new ArrayList<>();
        Set<User> userWithInterest = new HashSet<>();
        for(int i = 0; i < 3; i++){
            Event event = new Event();
            event.set_setInterests(new HashSet<>());
            eventsWithInterest.add(event);
        }
        for(int i = 0; i < 5; i++)
            userWithInterest.add(new User());
        Mockito.when(_interestRepository.findById(1)).thenReturn(Optional.of(interest));
        Mockito.when(interest.get_setUsers()).thenReturn(userWithInterest);
        Mockito.when(_eventRepository.findByInterest(any(Interest.class))).thenReturn(eventsWithInterest);
        //when
        ResultActions resultActions = _mockMvc.perform(delete("/api/deleteInterest/1"));
        //then
        resultActions.andDo(print())
                .andExpect(status().is2xxSuccessful());
        Mockito.verify(_userRepository, Mockito.times(5)).save(any(User.class));
        Mockito.verify(_eventRepository, Mockito.times(3)).save(any(Event.class));
        Mockito.verify(_interestRepository).deleteById(1);
    }
}
