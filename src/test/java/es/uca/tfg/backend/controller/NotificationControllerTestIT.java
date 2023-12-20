package es.uca.tfg.backend.integration.controller;

import es.uca.tfg.backend.config.AbstractTest;
import es.uca.tfg.backend.entity.*;
import es.uca.tfg.backend.rest.NotificationDTO;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest
@RunWith(SpringRunner.class)
public class NotificationControllerTestIT extends AbstractTest {

    @Override
    @Before
    public void setUp() {
        super.setUp();
    }

    @Test
    public void createNotificationWillCreateNewFollowNotification() throws Exception {
        //given
        NotificationDTO notificationDTO = new NotificationDTO("test", 1, 2, 0, 0, "NewFollow");
        User recipient = Mockito.mock(User.class);
        User issuer = Mockito.mock(User.class);
        TypeNotification typeNotification = Mockito.mock(TypeNotification.class);
        Mockito.when(recipient.get_iId()).thenReturn(0);
        Mockito.when(issuer.get_iId()).thenReturn(1);
        Mockito.when(_userRepository.findById(1)).thenReturn(Optional.of(recipient));
        Mockito.when(_userRepository.findById(2)).thenReturn(Optional.of(issuer));
        Mockito.when(_typeNotificationRepository.findBy_sName(any(String.class))).thenReturn(typeNotification);
        //when
        ResultActions response = _mockMvc.perform(post("/api/newNotification")
                .contentType(MediaType.APPLICATION_JSON)
                .content(_objectMapper.writeValueAsString(notificationDTO)));
        //then
        response.andDo(print())
                .andExpect(status().is2xxSuccessful());
        Mockito.verify(_notificationRepository).save(any(Notification.class));
    }
    @Test
    public void createNotificationWillCreateNewEventAssistantNotification() throws Exception {
        //given
        NotificationDTO notificationDTO = new NotificationDTO("test", 1, 2, 0, 1, "NewEventAssistant");
        User recipient = Mockito.mock(User.class);
        User issuer = Mockito.mock(User.class);
        Event event = Mockito.mock(Event.class);
        TypeNotification typeNotification = Mockito.mock(TypeNotification.class);
        Mockito.when(recipient.get_iId()).thenReturn(0);
        Mockito.when(issuer.get_iId()).thenReturn(1);
        Mockito.when(event.get_iId()).thenReturn(1);
        Mockito.when(_userRepository.findById(1)).thenReturn(Optional.of(recipient));
        Mockito.when(_userRepository.findById(2)).thenReturn(Optional.of(issuer));
        Mockito.when(_eventRepository.findById(1)).thenReturn(Optional.of(event));
        Mockito.when(_typeNotificationRepository.findBy_sName(any(String.class))).thenReturn(typeNotification);
        //when
        ResultActions response = _mockMvc.perform(post("/api/newNotification")
                .contentType(MediaType.APPLICATION_JSON)
                .content(_objectMapper.writeValueAsString(notificationDTO)));
        //then
        response.andDo(print())
                .andExpect(status().is2xxSuccessful());
        Mockito.verify(_notificationRepository).save(any(Notification.class));
    }

    @Test
    public void createNotificationWillFailWhenCreatingNewEventAssistantNotification() throws Exception {
        //given
        NotificationDTO notificationDTO = new NotificationDTO("test", 1, 2, 0, 0, "NewEventAssistant");
        User recipient = Mockito.mock(User.class);
        User issuer = Mockito.mock(User.class);
        Event event = Mockito.mock(Event.class);
        TypeNotification typeNotification = Mockito.mock(TypeNotification.class);
        Mockito.when(recipient.get_iId()).thenReturn(0);
        Mockito.when(issuer.get_iId()).thenReturn(1);
        Mockito.when(event.get_iId()).thenReturn(1);
        Mockito.when(_userRepository.findById(1)).thenReturn(Optional.of(recipient));
        Mockito.when(_userRepository.findById(2)).thenReturn(Optional.of(issuer));
        Mockito.when(_eventRepository.findById(1)).thenReturn(Optional.of(event));
        Mockito.when(_typeNotificationRepository.findBy_sName(any(String.class))).thenReturn(typeNotification);
        //when
        ResultActions response = _mockMvc.perform(post("/api/newNotification")
                .contentType(MediaType.APPLICATION_JSON)
                .content(_objectMapper.writeValueAsString(notificationDTO)));
        //then
        response.andDo(print())
                .andExpect(status().is2xxSuccessful());
        Mockito.verify(_notificationRepository, Mockito.never()).save(any(Notification.class));
    }

    @Test
    public void getUserNotificationsTest() throws Exception {
        //given
        //User user = Mockito.mock(User.class);
        User user = new User("example@gmail.com", "password", "username", "description", "user", "name", new Date(), new Province());
        Notification notification = new Notification("test", user, new TypeNotification("test"));

        //Page<Notification> notificationPage = new PageImpl<>(List.of());
        Page<Notification> notificationPage = new PageImpl(List.of(notification));
        Mockito.when(_userRepository.findBy_iId(1)).thenReturn(user);
        Mockito.when(_notificationRepository.findUserNotifications(any(User.class), any(Pageable.class))).thenReturn(notificationPage);
        //when
        ResultActions response = _mockMvc.perform(get("/api/getUserNotifications/1/0"));
        //then
        response.andDo(print())
                .andExpect(status().is2xxSuccessful());
        Assertions.assertNotNull(response.andReturn().getResponse().getContentAsString());
    }

    @Test
    public void getNotificationTest() throws Exception {
        //given
        User user = new User("example@gmail.com", "password", "username", "description", "user", "name", new Date(), new Province());
        TypeNotification typeNotification = new TypeNotification("test");
        Notification notification = new Notification("test", user, typeNotification);
        Mockito.when(_notificationRepository.findById(any(Integer.class))).thenReturn(Optional.of(notification));
        //when
        ResultActions response = _mockMvc.perform(get("/api/getNotification/1"));
        //then
        response.andDo(print())
                .andExpect(status().is2xxSuccessful());
        Assertions.assertEquals("test", _objectMapper.readValue(response.andReturn().getResponse().getContentAsString(), Notification.class).get_sInfo());
    }

    @Test
    public void checkPendingFollowTestWhenFollowIsPending() throws Exception {
        //given
        Notification followRequestNotification = Mockito.mock(Notification.class);

        TypeNotification followRequest = Mockito.mock(TypeNotification.class);
        TypeNotification followRequestAccepted = Mockito.mock(TypeNotification.class);
        User issuer = Mockito.mock(User.class);
        User recipient = Mockito.mock(User.class);
        Mockito.when(_userRepository.findById(1)).thenReturn(Optional.of(issuer));
        Mockito.when(_userRepository.findById(2)).thenReturn(Optional.of(recipient));
        Mockito.when(_typeNotificationRepository.findBy_sName("FollowRequest")).thenReturn(followRequest);
        Mockito.when(_typeNotificationRepository.findBy_sName("FollowRequestAccepted")).thenReturn(followRequestAccepted);
        Mockito.when(_notificationRepository.findByIssuerAndRecipientAndType(issuer, recipient, followRequest)).thenReturn(Optional.of(followRequestNotification));
        //when
        ResultActions response = _mockMvc.perform(get("/api/checkPendingFollow/1/2"));
        //then
        response.andDo(print())
                .andExpect(status().is2xxSuccessful());
        Assertions.assertEquals(true, _objectMapper.readValue(response.andReturn().getResponse().getContentAsString(), Boolean.class));
    }

    @Test
    public void checkPendingFollowTestWhenFollowIsNotPending() throws Exception {
        //given
        Notification followRequestNotification = Mockito.mock(Notification.class);
        Notification followRequestAcceptedNotification = Mockito.mock(Notification.class);

        TypeNotification followRequest = Mockito.mock(TypeNotification.class);
        TypeNotification followRequestAccepted = Mockito.mock(TypeNotification.class);
        User issuer = Mockito.mock(User.class);
        User recipient = Mockito.mock(User.class);

        Mockito.when(_userRepository.findById(1)).thenReturn(Optional.of(issuer));
        Mockito.when(_userRepository.findById(2)).thenReturn(Optional.of(recipient));
        Mockito.when(_typeNotificationRepository.findBy_sName("FollowRequest")).thenReturn(followRequest);
        Mockito.when(_typeNotificationRepository.findBy_sName("FollowRequestAccepted")).thenReturn(followRequestAccepted);
        Mockito.when(_notificationRepository.findByIssuerAndRecipientAndType(issuer, recipient, followRequest)).thenReturn(Optional.of(followRequestNotification));
        Mockito.when(_notificationRepository.findByIssuerAndRecipientAndType(recipient, issuer, followRequestAccepted)).thenReturn(Optional.of(followRequestAcceptedNotification));
        //when
        ResultActions response = _mockMvc.perform(get("/api/checkPendingFollow/1/2"));
        //then
        response.andDo(print())
                .andExpect(status().is2xxSuccessful());
        Assertions.assertEquals(false, _objectMapper.readValue(response.andReturn().getResponse().getContentAsString(), Boolean.class));
    }

    @Test
    public void deleteNotificationByIssuerRecipientAndTypeWillDeleteNotification() throws Exception {
        //given
        User issuer = Mockito.mock(User.class);
        User recipient = Mockito.mock(User.class);
        Notification notification = Mockito.mock(Notification.class);
        TypeNotification typeNotification = Mockito.mock(TypeNotification.class);
        Mockito.when(_userRepository.findById(1)).thenReturn(Optional.of(issuer));
        Mockito.when(_userRepository.findById(2)).thenReturn(Optional.of(recipient));
        Mockito.when(_typeNotificationRepository.findBy_sName(any(String.class))).thenReturn(typeNotification);
        Mockito.when(_notificationRepository.findByIssuerAndRecipientAndType(issuer, recipient, typeNotification)).thenReturn(Optional.of(notification));
        //when
        ResultActions response = _mockMvc.perform(patch("/api/deleteNotification/1/2/NewFollow"));
        //then
        response.andDo(print())
                .andExpect(status().is2xxSuccessful());
        Mockito.verify(_notificationRepository).delete(any(Notification.class));
    }

    @Test
    public void deleteNotificationByIdWillDeleteNotification() throws Exception {
        //given
        Notification notification = Mockito.mock(Notification.class);
        Mockito.when(_notificationRepository.findById(any(Integer.class))).thenReturn(Optional.of(notification));
        //when
        ResultActions response = _mockMvc.perform(patch("/api/deleteNotificationById/1"));
        //then
        response.andDo(print())
                .andExpect(status().is2xxSuccessful());
        Mockito.verify(_notificationRepository).delete(any(Notification.class));
    }
}
