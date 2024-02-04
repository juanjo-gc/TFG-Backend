package es.uca.tfg.backend.controller;

import es.uca.tfg.backend.config.AbstractTest;
import es.uca.tfg.backend.entity.*;
import es.uca.tfg.backend.repository.*;
import es.uca.tfg.backend.rest.NotificationDTO;
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
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;

@WebMvcTest
@RunWith(SpringRunner.class)
@ExtendWith(MockitoExtension.class)
public class NotificationControllerTestUT extends AbstractTest {

    @Override
    @Before
    public void setUp() {
        super.setUp();
    }

    @Mock
    private NotificationRepository _notificationRepository;
    @Mock
    private TypeNotificationRepository _typeNotificationRepository;
    @Mock
    private UserRepository _userRepository;
    @Mock
    private EventRepository _eventRepository;
    @Mock
    private PostRepository _postRepository;
    @Mock
    private PersonRepository _personRepository;

    @InjectMocks
    private NotificationController _controller;

    @Mock
    private User _issuer;
    @Mock
    private User _recipient;

    @Test
    public void newNotificationWillCreateNewFollowTypeNotification() {
        //given
        Notification notification = new Notification();
        TypeNotification typeNotification = new TypeNotification("NewFollow");
        NotificationDTO dto = new NotificationDTO("Test notification", 2, 1, -1, -1, "NewFollow");
        Mockito.when(_personRepository.findById(1)).thenReturn(Optional.of(_issuer));
        Mockito.when(_userRepository.findById(2)).thenReturn(Optional.of(_recipient));
        Mockito.when(_typeNotificationRepository.findBy_sName("NewFollow")).thenReturn(typeNotification);
        Mockito.when(_notificationRepository.save(any(Notification.class))).thenReturn(notification);
        //when
        Notification newNotification = _controller.createNotification(dto);
        //then
        Mockito.verify(_notificationRepository).save(any(Notification.class));
        Mockito.verify(_typeNotificationRepository).findBy_sName("NewFollow");
    }

    @Test
    public void newNotificationWillCreateFollowRequestTypeNotification() {
        //given
        Notification notification = new Notification();
        TypeNotification typeNotification = new TypeNotification("FollowRequest");
        NotificationDTO dto = new NotificationDTO("Test notification", 2, 1, -1, -1, "FollowRequest");
        Mockito.when(_personRepository.findById(1)).thenReturn(Optional.of(_issuer));
        Mockito.when(_userRepository.findById(2)).thenReturn(Optional.of(_recipient));
        Mockito.when(_typeNotificationRepository.findBy_sName("FollowRequest")).thenReturn(typeNotification);
        Mockito.when(_notificationRepository.save(any(Notification.class))).thenReturn(notification);
        //when
        Notification newNotification = _controller.createNotification(dto);
        //then
        Mockito.verify(_notificationRepository).save(any(Notification.class));
        Mockito.verify(_typeNotificationRepository).findBy_sName("FollowRequest");
    }

    @Test
    public void newNotificationWillCreateFollowRequestAcceptedTypeNotification() {
        //given
        Notification notification = new Notification();
        TypeNotification typeNotification = new TypeNotification("FollowRequestAccepted");
        NotificationDTO dto = new NotificationDTO("Test notification", 2, 1, -1, -1, "FollowRequestAccepted");
        Mockito.when(_personRepository.findById(1)).thenReturn(Optional.of(_issuer));
        Mockito.when(_userRepository.findById(2)).thenReturn(Optional.of(_recipient));
        Mockito.when(_typeNotificationRepository.findBy_sName("FollowRequestAccepted")).thenReturn(typeNotification);
        Mockito.when(_notificationRepository.save(any(Notification.class))).thenReturn(notification);
        //when
        Notification newNotification = _controller.createNotification(dto);
        //then
        Mockito.verify(_notificationRepository).save(any(Notification.class));
        Mockito.verify(_typeNotificationRepository).findBy_sName("FollowRequestAccepted");
    }

    @Test
    public void newNotificationWillCreateBehaviorWarningTypeNotification() {
        //given
        Notification notification = new Notification();
        TypeNotification typeNotification = new TypeNotification("BehaviorWarning");
        NotificationDTO dto = new NotificationDTO("Test notification", 2, 1, -1, -1, "BehaviorWarning");
        Mockito.when(_personRepository.findById(1)).thenReturn(Optional.of(_issuer));
        Mockito.when(_userRepository.findById(2)).thenReturn(Optional.of(_recipient));
        Mockito.when(_typeNotificationRepository.findBy_sName("BehaviorWarning")).thenReturn(typeNotification);
        Mockito.when(_notificationRepository.save(any(Notification.class))).thenReturn(notification);
        //when
        Notification newNotification = _controller.createNotification(dto);
        //then
        Mockito.verify(_notificationRepository).save(any(Notification.class));
        Mockito.verify(_typeNotificationRepository).findBy_sName("BehaviorWarning");
    }

    @Test
    public void newNotificationWillCreateAnnouncementTypeNotification() {
        //given
        Notification notification = new Notification();
        TypeNotification typeNotification = new TypeNotification("Announcement");
        NotificationDTO dto = new NotificationDTO("Test notification", 2, 1, -1, -1, "Announcement");
        Mockito.when(_personRepository.findById(1)).thenReturn(Optional.of(_issuer));
        Mockito.when(_userRepository.findById(2)).thenReturn(Optional.of(_recipient));
        Mockito.when(_typeNotificationRepository.findBy_sName("Announcement")).thenReturn(typeNotification);
        Mockito.when(_notificationRepository.save(any(Notification.class))).thenReturn(notification);
        //when
        Notification newNotification = _controller.createNotification(dto);
        //then
        Mockito.verify(_notificationRepository).save(any(Notification.class));
        Mockito.verify(_typeNotificationRepository).findBy_sName("Announcement");
    }

    @Test
    public void newNotificationWillCreateNewEventAssistantTypeNotification() {
        //given
        Notification notification = new Notification();
        Event event = Mockito.mock(Event.class);
        TypeNotification typeNotification = new TypeNotification("NewEventAssistant");
        NotificationDTO dto = new NotificationDTO("Test notification", 2, 1, -1, 1, "NewEventAssistant");
        Mockito.when(_personRepository.findById(1)).thenReturn(Optional.of(_issuer));
        Mockito.when(_userRepository.findById(2)).thenReturn(Optional.of(_recipient));
        Mockito.when(_eventRepository.findById(1)).thenReturn(Optional.of(event));
        Mockito.when(_typeNotificationRepository.findBy_sName("NewEventAssistant")).thenReturn(typeNotification);
        Mockito.when(_notificationRepository.save(any(Notification.class))).thenReturn(notification);
        //when
        Notification newNotification = _controller.createNotification(dto);
        //then
        Mockito.verify(_notificationRepository).save(any(Notification.class));
        Mockito.verify(_typeNotificationRepository).findBy_sName("NewEventAssistant");
    }

    @Test
    public void newNotificationWillCreateNewEventCommentTypeNotification() {
        //given
        Notification notification = new Notification();
        Event event = Mockito.mock(Event.class);
        TypeNotification typeNotification = new TypeNotification("NewEventComment");
        NotificationDTO dto = new NotificationDTO("Test notification", 2, 1, -1, 1, "NewEventComment");
        Mockito.when(_personRepository.findById(1)).thenReturn(Optional.of(_issuer));
        Mockito.when(_userRepository.findById(2)).thenReturn(Optional.of(_recipient));
        Mockito.when(_eventRepository.findById(1)).thenReturn(Optional.of(event));
        Mockito.when(_typeNotificationRepository.findBy_sName("NewEventComment")).thenReturn(typeNotification);
        Mockito.when(_notificationRepository.save(any(Notification.class))).thenReturn(notification);
        //when
        Notification newNotification = _controller.createNotification(dto);
        //then
        Mockito.verify(_notificationRepository).save(any(Notification.class));
        Mockito.verify(_typeNotificationRepository).findBy_sName("NewEventComment");
    }

    @Test
    public void newNotificationWillCreateNewEventPhotoTypeNotification() {
        //given
        Notification notification = new Notification();
        Event event = Mockito.mock(Event.class);
        TypeNotification typeNotification = new TypeNotification("NewEventPhoto");
        NotificationDTO dto = new NotificationDTO("Test notification", 2, 1, -1, 1, "NewEventPhoto");
        Mockito.when(_personRepository.findById(1)).thenReturn(Optional.of(_issuer));
        Mockito.when(_userRepository.findById(2)).thenReturn(Optional.of(_recipient));
        Mockito.when(_eventRepository.findById(1)).thenReturn(Optional.of(event));
        Mockito.when(_typeNotificationRepository.findBy_sName("NewEventPhoto")).thenReturn(typeNotification);
        Mockito.when(_notificationRepository.save(any(Notification.class))).thenReturn(notification);
        //when
        Notification newNotification = _controller.createNotification(dto);
        //then
        Mockito.verify(_notificationRepository).save(any(Notification.class));
        Mockito.verify(_typeNotificationRepository).findBy_sName("NewEventPhoto");
    }

    @Test
    public void newNotificationWillCreateNewMessagesTypeNotification() {
        //given
        Notification notification = new Notification();
        Event event = Mockito.mock(Event.class);
        TypeNotification typeNotification = new TypeNotification("NewMessages");
        NotificationDTO dto = new NotificationDTO("Test notification", 1, -1, -1, -1, "NewMessages");
        Mockito.when(_userRepository.findById(1)).thenReturn(Optional.of(_recipient));
        Mockito.when(_typeNotificationRepository.findBy_sName("NewMessages")).thenReturn(typeNotification);
        Mockito.when(_notificationRepository.save(any(Notification.class))).thenReturn(notification);
        //when
        Notification newNotification = _controller.createNotification(dto);
        //then
        Mockito.verify(_notificationRepository).save(any(Notification.class));
        Mockito.verify(_typeNotificationRepository).findBy_sName("NewMessages");
    }

    @Test
    public void newNotificationWillCreateNewPostLikeTypeNotification() {
        //given
        Notification notification = new Notification();
        Post post = Mockito.mock(Post.class);
        TypeNotification typeNotification = new TypeNotification("NewPostLike");
        NotificationDTO dto = new NotificationDTO("Test notification", 2, 1, 1, -1, "NewPostLike");
        Mockito.when(_personRepository.findById(1)).thenReturn(Optional.of(_issuer));
        Mockito.when(_userRepository.findById(2)).thenReturn(Optional.of(_recipient));
        Mockito.when(_postRepository.findById(1)).thenReturn(Optional.of(post));
        Mockito.when(_typeNotificationRepository.findBy_sName("NewPostLike")).thenReturn(typeNotification);
        Mockito.when(_notificationRepository.save(any(Notification.class))).thenReturn(notification);
        //when
        Notification newNotification = _controller.createNotification(dto);
        //then
        Mockito.verify(_notificationRepository).save(any(Notification.class));
        Mockito.verify(_typeNotificationRepository).findBy_sName("NewPostLike");
    }

    @Test
    public void newNotificationWillCreateNewPostCommentTypeNotification() {
        //given
        Notification notification = new Notification();
        Post post = Mockito.mock(Post.class);
        TypeNotification typeNotification = new TypeNotification("NewPostComment");
        NotificationDTO dto = new NotificationDTO("Test notification", 2, 1, 1, -1, "NewPostComment");
        Mockito.when(_personRepository.findById(1)).thenReturn(Optional.of(_issuer));
        Mockito.when(_userRepository.findById(2)).thenReturn(Optional.of(_recipient));
        Mockito.when(_postRepository.findById(1)).thenReturn(Optional.of(post));
        Mockito.when(_typeNotificationRepository.findBy_sName("NewPostComment")).thenReturn(typeNotification);
        Mockito.when(_notificationRepository.save(any(Notification.class))).thenReturn(notification);
        //when
        Notification newNotification = _controller.createNotification(dto);
        //then
        Mockito.verify(_notificationRepository).save(any(Notification.class));
        Mockito.verify(_typeNotificationRepository).findBy_sName("NewPostComment");
    }

    @Test
    public void checkPendingFollowWillReturnTrueAsUserFollowIsPending() {
        //given
        TypeNotification typeNotification = new TypeNotification();
        Mockito.when(_userRepository.findById(1)).thenReturn(Optional.of(_issuer));
        Mockito.when(_userRepository.findById(2)).thenReturn(Optional.of(_recipient));
        Mockito.when(_typeNotificationRepository.findBy_sName("FollowRequest")).thenReturn(typeNotification);
        Mockito.when(_notificationRepository.findByIssuerAndRecipientAndType(_issuer, _recipient, typeNotification)).thenReturn(Optional.of(new Notification()));
        //when
        boolean bIsPending = _controller.checkPendingFollow(1, 2);
        //then
        //Mockito.verify(_notificationRepository, Mockito.times(2)).findByIssuerAndRecipientAndType(any(User.class), any(User.class), any(TypeNotification.class));
        Assertions.assertTrue(bIsPending);
    }

    @Test
    public void checkPendingFollowWillReturnFalseAsUserAlreadyFollowsTargetUser() {
        //given
        Notification notification = new Notification();
        TypeNotification followRequest = new TypeNotification("FollowRequest");
        TypeNotification followRequestAccepted = new TypeNotification("FollowRequestAccepted");
        Mockito.when(_userRepository.findById(1)).thenReturn(Optional.of(_issuer));
        Mockito.when(_userRepository.findById(2)).thenReturn(Optional.of(_recipient));
        Mockito.when(_typeNotificationRepository.findBy_sName("FollowRequest")).thenReturn(followRequest);
        Mockito.when(_typeNotificationRepository.findBy_sName("FollowRequestAccepted")).thenReturn(followRequestAccepted);
        Mockito.when(_notificationRepository.findByIssuerAndRecipientAndType(_issuer, _recipient, followRequest)).thenReturn(Optional.of(notification));
        Mockito.when(_notificationRepository.findByIssuerAndRecipientAndType(_recipient, _issuer, followRequestAccepted)).thenReturn(Optional.of(notification));
        //when
        boolean bIsPending = _controller.checkPendingFollow(1, 2);
        //then
        Mockito.verify(_notificationRepository, Mockito.times(2)).findByIssuerAndRecipientAndType(any(User.class), any(User.class), any(TypeNotification.class));
        Assertions.assertFalse(bIsPending);
    }

    @Test
    public void deleteNotificationByIdWillDeleteNotification() {
        //given
        Notification notification = new Notification();
        Mockito.when(_notificationRepository.findById(1)).thenReturn(Optional.of(notification));
        //when
        _controller.deleteNotificatinById(1);
        //then
        Mockito.verify(_notificationRepository).delete(notification);
    }

    @Test
    public void deleteNotificationByIdWillFailAsNotificationDoesNotExist() {
        //given
        Notification notification = new Notification();
        //when
        _controller.deleteNotificatinById(1);
        //then
        Mockito.verify(_notificationRepository, Mockito.times(0)).delete(notification);
    }

    @Test
    public void getUserWarningsWillReturnListOfWarnings() {
        //given
        List<Notification> aWarnings = List.of(new Notification(), new Notification());
        Mockito.when(_userRepository.findById(1)).thenReturn(Optional.of(_recipient));
        Mockito.when(_notificationRepository.findUserWarnings(_recipient)).thenReturn(aWarnings);
        //when
        List<Notification> aUserWarnings = _controller.getUserWarnings(1);
        //then
        Mockito.verify(_notificationRepository).findUserWarnings(_recipient);
        Assertions.assertEquals(2, aUserWarnings.size());
    }

    @Test
    public void getUserWarningsWillFailAndReturnEmptyListAsUserDoesNotExist() {
        //given
        List<Notification> aWarnings = List.of(new Notification(), new Notification());
        Mockito.when(_notificationRepository.findUserWarnings(_recipient)).thenReturn(aWarnings);
        //when
        List<Notification> aUserWarnings = _controller.getUserWarnings(1);
        //then
        Mockito.verify(_notificationRepository, Mockito.times(0)).findUserWarnings(_recipient);
        Assertions.assertEquals(0, aUserWarnings.size());
    }

    @Test
    public void countNewNotificationsWillReturnNumberOfUnseenNotificatios() {
        //given
        Mockito.when(_userRepository.findById(1)).thenReturn(Optional.of(_recipient));
        Mockito.when(_notificationRepository.countNewNotifications(_recipient)).thenReturn(5);
        //when
        int iUnseenNotifications = _controller.countNewNotifications(1);
        //then
        Mockito.verify(_notificationRepository).countNewNotifications(_recipient);
        Assertions.assertEquals(5, iUnseenNotifications);
    }
    @Test
    public void countNewNotificationsWillFailAndReturnOutOfDomainValueAsUserDoesNotExist() {
        //given
        Mockito.when(_notificationRepository.countNewNotifications(_recipient)).thenReturn(5);
        //when
        int iUnseenNotifications = _controller.countNewNotifications(1);
        //then
        Mockito.verify(_notificationRepository, Mockito.times(0)).countNewNotifications(_recipient);
        Assertions.assertEquals(-1, iUnseenNotifications);
    }

    @Test
    public void setSeenWillSetNotificationAsSeen() {
        //given
        Notification notification = new Notification();
        notification.set_bSeen(false);
        Mockito.when(_notificationRepository.findById(1)).thenReturn(Optional.of(notification));
        //when
        _controller.setSeen(1);
        //then
        Mockito.verify(_notificationRepository).save(notification);
        Assertions.assertTrue(notification.is_bSeen());
    }

    @Test
    public void setSeenWillFailAndWontModifyAnythingAsNotificationDoesNotExist() {
        //given
        Notification notification = Mockito.mock(Notification.class);
        //when
        _controller.setSeen(1);
        //then
        Mockito.verify(_notificationRepository, Mockito.times(0)).save(notification);
        Mockito.verify(notification, Mockito.times(0)).set_bSeen(anyBoolean()  );
    }
}
