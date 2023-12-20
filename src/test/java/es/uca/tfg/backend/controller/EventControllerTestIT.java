package es.uca.tfg.backend.integration.controller;


import es.uca.tfg.backend.config.AbstractTest;
import es.uca.tfg.backend.entity.*;
import es.uca.tfg.backend.rest.EventDTO;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.ResultActions;


import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest
@RunWith(SpringRunner.class)
public class EventControllerTestIT extends AbstractTest {

    @Override
    @Before
    public void setUp() {
        super.setUp();
    }

    @Test
    public void newNonOnlineEventWillRegisterLocationAndCreateEvent() throws Exception {
        //given
        EventDTO eventDTO = new EventDTO("TestEvent", LocalDate.now(), LocalTime.now(), "Description", Integer.valueOf(1),
                new HashSet<>(Arrays.asList("i1", "i2")), "Location", 1.2f, 2.1f, "Province", false);

        User organizer = Mockito.mock(User.class);
        Interest i1 = Mockito.mock(Interest.class);
        Interest i2 = Mockito.mock(Interest.class);
        Event event = Mockito.mock(Event.class);
        Mockito.when(_userRepository.findBy_iId(1)).thenReturn(organizer);
        Mockito.when(_interestRepository.findBy_sName("i1")).thenReturn(i1);
        Mockito.when(_interestRepository.findBy_sName("i2")).thenReturn(i2);
        Mockito.when(_eventRepository.save(any(Event.class))).thenReturn(event);
        Mockito.when(event.get_iId()).thenReturn(10);
        //when
        ResultActions resultActions = _mockMvc.perform(post("/api/newEvent")
                .contentType(MediaType.APPLICATION_JSON)
                .content(_objectMapper.writeValueAsString(eventDTO)));
        //then
        resultActions.andDo(print())
                .andExpect(status().is2xxSuccessful());
        Assertions.assertEquals(10, _objectMapper.readValue(resultActions.andReturn().getResponse().getContentAsString(), Integer.class));
        Mockito.verify(_eventRepository).save(any(Event.class));
        Mockito.verify(_locationRepository).save(any(Location.class));
    }

    @Test
    public void newNonOnlineEventWillCreateEventInRegisteredLocation() throws Exception {
        //given
        Set<String> asInterests = new HashSet<>(Arrays.asList("i1", "i2"));
        EventDTO eventDTO = new EventDTO("TestTitle", LocalDate.now(), LocalTime.now(), "Description", 1,
                asInterests, "RegisteredLocation", 5.0f, 5.0f, "Province", false);
        System.out.println("Titulo: " +eventDTO.get_sTitle() + " Organizer id: " + eventDTO.get_iOrganizerId());
        System.out.println(_objectMapper.writeValueAsString(eventDTO));
        User organizer = Mockito.mock(User.class);
        EventDTO mock = Mockito.mock(EventDTO.class);
        Interest i1 = Mockito.mock(Interest.class);
        Interest i2 = Mockito.mock(Interest.class);
        Location location = Mockito.mock(Location.class);
        Province province = Mockito.mock(Province.class);
        Event event = Mockito.mock(Event.class);
        Mockito.when(_userRepository.findBy_iId(1)).thenReturn(organizer);
        Mockito.when(_interestRepository.findBy_sName("i1")).thenReturn(i1);
        Mockito.when(_interestRepository.findBy_sName("i2")).thenReturn(i2);
        Mockito.when(_locationRepository.findBy_sName(any(String.class))).thenReturn(Optional.of(location));
        Mockito.when(_provinceRepository.findBy_sName(any(String.class))).thenReturn(province);
        Mockito.when(_eventRepository.save(any(Event.class))).thenReturn(event);
        Mockito.when(event.get_iId()).thenReturn(10);
        //when
        ResultActions resultActions = _mockMvc.perform(post("/api/newEvent")
                .contentType(MediaType.APPLICATION_JSON)
                .content(_objectMapper.writeValueAsString(eventDTO)));

        //then
        resultActions.andDo(print())
                .andExpect(status().is2xxSuccessful());
        Mockito.verify(_eventRepository).save(any(Event.class));
        Mockito.verify(_locationRepository, Mockito.never()).save(any(Location.class));
    }

    @Test
    public void getEventWillReturnExistingEvent() throws Exception {
        //given
        Event event = new Event("TestTitle", LocalDate.now(), LocalTime.now(), "Description", new User(), Collections.emptySet(), null, null, true);
        Mockito.when(_eventRepository.findById(any(Integer.class))).thenReturn(Optional.of(event));
        //when
        ResultActions resultActions = _mockMvc.perform(get("/api/getEvent/1"));
        //then
        resultActions.andDo(print())
                .andExpect(status().is2xxSuccessful());
        Assertions.assertEquals(event.get_iId(), _objectMapper.readValue(resultActions.andReturn().getResponse().getContentAsString(), Event.class).get_iId());
    }

    @Test
    public void getEventWillReturnNonExistingEvent() throws Exception {
        //given
        //when
        ResultActions resultActions = _mockMvc.perform(get("/api/getEvent/1"));
        //then
        resultActions.andDo(print())
                .andExpect(status().is2xxSuccessful());
        Assertions.assertEquals(0, _objectMapper.readValue(resultActions.andReturn().getResponse().getContentAsString(), Event.class).get_iId());
    }

    @Test
    public void getEventAssistantsWillReturnAssistantsList() throws Exception {
        //given
        Event event = Mockito.mock(Event.class);
        Set<User> aAssistants = Set.of(new User(), new User());
        Mockito.when(_eventRepository.findById(any(Integer.class))).thenReturn(Optional.of(event));
        Mockito.when(event.get_setAssistants()).thenReturn(aAssistants);
        //when
        ResultActions resultActions = _mockMvc.perform(get("/api/getEventAssistants/1"));
        //then
        resultActions.andDo(print())
                .andExpect(status().is2xxSuccessful());
        Assertions.assertEquals(aAssistants.size(), _objectMapper.readValue(resultActions.andReturn().getResponse().getContentAsString(), List.class).size());
    }

    @Test
    public void getEventAssistantIdsWillReturnListOfIds() throws Exception {
        //given
        Event event = Mockito.mock(Event.class);
        List<Integer> aiAssistantIds = List.of(1, 2, 3, 4, 5);
        Mockito.when(_eventRepository.findById(any(Integer.class))).thenReturn(Optional.of(event));
        Mockito.when(_eventRepository.findAssistantIds(any(Integer.class))).thenReturn(aiAssistantIds);
        //when
        ResultActions resultActions = _mockMvc.perform(get("/api/getEventAssistantIds/1"));
        //then
        resultActions.andDo(print())
                .andExpect(status().is2xxSuccessful());
        Mockito.verify(_eventRepository).findAssistantIds(any(Integer.class));
        Assertions.assertEquals(aiAssistantIds.size(), _objectMapper.readValue(resultActions.andReturn().getResponse().getContentAsString(), List.class).size());
    }

    @Test
    public void setAssistWillAddAssistant() throws Exception {
        //given
        Event event = Mockito.mock(Event.class);
        User user = Mockito.mock(User.class);
        Set<User> aAssistants = Mockito.mock(Set.class);
        Mockito.when(_userRepository.findById(any(Integer.class))).thenReturn(Optional.of(user));
        Mockito.when(_eventRepository.findById(any(Integer.class))).thenReturn(Optional.of(event));
        Mockito.when(event.get_setAssistants()).thenReturn(aAssistants);
        Mockito.when(aAssistants.contains(any())).thenReturn(false);
        Mockito.when(aAssistants.add(any())).thenReturn(true);
        //when
        ResultActions resultActions = _mockMvc.perform(patch( "/api/setAssist/1/1"));
        //then
        resultActions.andDo(print())
                .andExpect(status().is2xxSuccessful());
        Mockito.verify(_eventRepository).save(any(Event.class));
        Assertions.assertEquals(true, _objectMapper.readValue(resultActions.andReturn().getResponse().getContentAsString(), Boolean.class));
    }

    @Test
    public void setAssistWillRemoveAssistant() throws Exception {
        //given
        Event event = Mockito.mock(Event.class);
        User user = Mockito.mock(User.class);
        Set<User> aAssistants = Mockito.mock(Set.class);
        Mockito.when(_userRepository.findById(any(Integer.class))).thenReturn(Optional.of(user));
        Mockito.when(_eventRepository.findById(any(Integer.class))).thenReturn(Optional.of(event));
        Mockito.when(event.get_setAssistants()).thenReturn(aAssistants);
        Mockito.when(aAssistants.contains(any())).thenReturn(true);
        //when
        ResultActions resultActions = _mockMvc.perform(patch( "/api/setAssist/1/1"));
        //then
        resultActions.andDo(print())
                .andExpect(status().is2xxSuccessful());
        Mockito.verify(_eventRepository).save(any(Event.class));
        Assertions.assertEquals(false, _objectMapper.readValue(resultActions.andReturn().getResponse().getContentAsString(), Boolean.class));
    }

    @Test
    public void getFilteredAssistantsWillReturnFilteredUserByNames() throws Exception {
        //given
        Event event = Mockito.mock(Event.class);
        List<User> aFilteredUsers = List.of(new User(), new User());
        Mockito.when(_eventRepository.findById(anyInt())).thenReturn(Optional.of(event));
        Mockito.when(_eventRepository.findFilteredAssistantsByName(anyString())).thenReturn(aFilteredUsers);
        //when
        ResultActions resultActions = _mockMvc.perform(get("/api/getFilteredAssistants/test/1"));
        //then
        resultActions.andDo(print())
                .andExpect(status().is2xxSuccessful());
        Assertions.assertEquals(aFilteredUsers.size(), _objectMapper.readValue(resultActions.andReturn().getResponse().getContentAsString(), List.class).size());
    }

    @Test
    public void updateLocatedEventWillUpdateExistingEventInExistingLocation() throws Exception {
        //given
        EventDTO eventDTO = new EventDTO("TestEvent", LocalDate.now(), LocalTime.now(), "Description", Integer.valueOf(1),
                new HashSet<>(Arrays.asList("i1", "i2")), "Location", 1.2f, 2.1f, "Province", false);
        Event event = Mockito.mock(Event.class);
        Location location = Mockito.mock(Location.class);
        Mockito.when(event.get_iId()).thenReturn(1);
        Mockito.when(_locationRepository.findBy_sName(anyString())).thenReturn(Optional.of(location));
        Mockito.when(_eventRepository.findById(anyInt())).thenReturn(Optional.of(event));
        Mockito.when(_eventRepository.save(any(Event.class))).thenReturn(event);
        //when
        ResultActions resultActions = _mockMvc.perform(post("/api/updateEvent/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(_objectMapper.writeValueAsString(eventDTO)));
        //then
        resultActions.andDo(print())
                .andExpect(status().is2xxSuccessful());
        Mockito.verify(_locationRepository, Mockito.never()).save(any(Location.class));
        Mockito.verify(_eventRepository).save(any(Event.class));
        Mockito.verify(event).set_sTitle(anyString());
        Assertions.assertEquals(event.get_iId(), _objectMapper.readValue(resultActions.andReturn().getResponse().getContentAsString(), Integer.class));

    }

    @Test
    public void updateLocatedEventWillUpdateExistingEventInNewLocation() throws Exception {
        //given
        EventDTO eventDTO = new EventDTO("TestEvent", LocalDate.now(), LocalTime.now(), "Description", Integer.valueOf(1),
                new HashSet<>(Arrays.asList("i1", "i2")), "Location", 1.2f, 2.1f, "Province", false);
        Event event = Mockito.mock(Event.class);
        Mockito.when(event.get_iId()).thenReturn(1);
        Mockito.when(_eventRepository.findById(anyInt())).thenReturn(Optional.of(event));
        Mockito.when(_eventRepository.save(any(Event.class))).thenReturn(event);
        //when
        ResultActions resultActions = _mockMvc.perform(post("/api/updateEvent/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(_objectMapper.writeValueAsString(eventDTO)));
        //then
        resultActions.andDo(print())
                .andExpect(status().is2xxSuccessful());
        Mockito.verify(_locationRepository).save(any(Location.class));
        Mockito.verify(_eventRepository).save(any(Event.class));
        Mockito.verify(event).set_sTitle(anyString());
        Assertions.assertEquals(event.get_iId(), _objectMapper.readValue(resultActions.andReturn().getResponse().getContentAsString(), Integer.class));
    }

    @Test
    public void updateOnlineEventWillUpdateExistingEvent() throws Exception {
        //given
        EventDTO eventDTO = new EventDTO("TestEvent", LocalDate.now(), LocalTime.now(), "Description", Integer.valueOf(1),
                new HashSet<>(Arrays.asList("i1", "i2")), "Location", 1.2f, 2.1f, "Province", true);
        Event event = Mockito.mock(Event.class);
        Mockito.when(event.get_iId()).thenReturn(1);
        Mockito.when(_eventRepository.findById(anyInt())).thenReturn(Optional.of(event));
        Mockito.when(_eventRepository.save(any(Event.class))).thenReturn(event);
        //when
        ResultActions resultActions = _mockMvc.perform(post("/api/updateEvent/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(_objectMapper.writeValueAsString(eventDTO)));
        //then
        resultActions.andDo(print())
                .andExpect(status().is2xxSuccessful());
        Mockito.verify(_locationRepository, Mockito.never()).findBy_sName(anyString());
        Mockito.verify(_eventRepository).save(any(Event.class));
        Mockito.verify(event).set_sTitle(anyString());
        Assertions.assertEquals(event.get_iId(), _objectMapper.readValue(resultActions.andReturn().getResponse().getContentAsString(), Integer.class));
    }
}
