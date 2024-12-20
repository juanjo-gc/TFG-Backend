package es.uca.tfg.backend.controller;

import es.uca.tfg.backend.entity.*;
import es.uca.tfg.backend.repository.*;
import es.uca.tfg.backend.rest.NotificationDTO;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class NotificationController {

    @Autowired
    private NotificationRepository _notificationRepository;
    @Autowired
    private TypeNotificationRepository _typeNotificationRepository;
    @Autowired
    private UserRepository _userRepository;
    @Autowired
    private EventRepository _eventRepository;
    @Autowired
    private PostRepository _postRepository;
    @Autowired
    private PersonRepository _personRepository;


    @PostMapping("/newNotification")
    public Notification createNotification(@RequestBody NotificationDTO notificationDTO) {
        Optional<User> optionalRecipient = _userRepository.findById(notificationDTO.get_iRecipientId());
        Optional<Person> optionalIssuer = _personRepository.findById(notificationDTO.get_iIssuerId());
        Optional<Event> optionalEvent = _eventRepository.findById(notificationDTO.get_iEventId());
        Optional<Post> optionalPost = _postRepository.findById(notificationDTO.get_iPostId());

        if (Objects.equals(notificationDTO.get_sType(), "NewFollow")) {
            if (optionalIssuer.isPresent() && optionalRecipient.isPresent()) {
                return _notificationRepository.save(new Notification(notificationDTO.get_sInfo(), optionalRecipient.get(),
                        _typeNotificationRepository.findBy_sName(notificationDTO.get_sType()), optionalIssuer.get()));
            } else {
                return new Notification();
            }
        } else if (Objects.equals(notificationDTO.get_sType(), "FollowRequest") || Objects.equals(notificationDTO.get_sType(), "FollowRequestAccepted")
                    || (Objects.equals(notificationDTO.get_sType(), "BehaviorWarning") || (Objects.equals(notificationDTO.get_sType(), "Announcement")))) {
            if (optionalIssuer.isPresent() && optionalRecipient.isPresent()) {
                return _notificationRepository.save(new Notification(notificationDTO.get_sInfo(), optionalRecipient.get(),
                        _typeNotificationRepository.findBy_sName(notificationDTO.get_sType()), optionalIssuer.get()));
            } else {
                return new Notification();
            }
        } else if (Objects.equals(notificationDTO.get_sType(), "NewEventAssistant") ||
                Objects.equals(notificationDTO.get_sType(), "NewEventComment") ||
                Objects.equals(notificationDTO.get_sType(), "NewEventPhoto")) {
            if (optionalIssuer.isPresent() && optionalRecipient.isPresent() && optionalEvent.isPresent()) {
                return _notificationRepository.save(new Notification(notificationDTO.get_sInfo(), optionalRecipient.get(),
                        _typeNotificationRepository.findBy_sName(notificationDTO.get_sType()), optionalIssuer.get(), optionalEvent.get()));
            } else {
                return new Notification();
            }
        } else if (Objects.equals(notificationDTO.get_sType(), "NewMessages")) {
            if (optionalRecipient.isPresent()) {
                return _notificationRepository.save(new Notification(notificationDTO.get_sInfo(), optionalRecipient.get(),
                        _typeNotificationRepository.findBy_sName(notificationDTO.get_sType())));
            } else {
                return new Notification();
            }
        } else if (Objects.equals(notificationDTO.get_sType(), "NewPostLike") ||
                Objects.equals(notificationDTO.get_sType(), "NewPostComment")) {
            if (optionalRecipient.isPresent() && optionalIssuer.isPresent() && optionalPost.isPresent()) {
                return _notificationRepository.save(new Notification(notificationDTO.get_sInfo(), optionalRecipient.get(),
                        _typeNotificationRepository.findBy_sName(notificationDTO.get_sType()), optionalIssuer.get(), optionalPost.get()));
            } else {
                return new Notification();
            }
        } else {
            return new Notification();
        }
    }

    @GetMapping("/getUserNotifications/{userId}/{pageNumber}")
    public Page<Notification> getUserNotifications(@PathVariable("userId") int iUserId, @PathVariable("pageNumber") int iPageNumber) {
        return  _notificationRepository.findUserNotifications(_userRepository.findBy_iId(iUserId), PageRequest.of(iPageNumber, 20));
    }
    @GetMapping("getNotification/{id}")
    public Notification getNotification(@PathVariable("id") int iId) {
        Optional<Notification> optionalNotification = _notificationRepository.findById(iId);
        return optionalNotification.isPresent() ? optionalNotification.get() : new Notification();
    }

    @GetMapping("/checkPendingFollow/{issuerId}/{recipientId}")
    public boolean checkPendingFollow(@PathVariable("issuerId") int iIssuerId,@PathVariable("recipientId") int iRecipientId) {
        Optional<User> optionalIssuer = _userRepository.findById(iIssuerId);
        Optional<User> optionalRecipient = _userRepository.findById(iRecipientId);
        //Para que esté pendiente la solicitud, deberá existir un FollowRequest, pero no un FollowRequestAccepted
        if(optionalIssuer.isPresent() && optionalRecipient.isPresent()) {
            return _notificationRepository.findByIssuerAndRecipientAndType(optionalIssuer.get(), optionalRecipient.get(), _typeNotificationRepository.findBy_sName("FollowRequest")).isPresent() &&
                    !_notificationRepository.findByIssuerAndRecipientAndType(optionalRecipient.get(), optionalIssuer.get(), _typeNotificationRepository.findBy_sName("FollowRequestAccepted")).isPresent();
        } else {
            return false;
        }
    }

    @PatchMapping("/deleteNotification/{issuerId}/{recipientId}/{type}")
    public void deleteNotification(@PathVariable("issuerId") int iIssuerId,@PathVariable("recipientId") int iRecipientId, @PathVariable("type") String sType) {
        Optional<User> optionalIssuer = _userRepository.findById(iIssuerId);
        Optional<User> optionalRecipient = _userRepository.findById(iRecipientId);
        if(optionalIssuer.isPresent() && optionalRecipient.isPresent()) {
            _notificationRepository.delete(_notificationRepository.findByIssuerAndRecipientAndType(optionalIssuer.get(), optionalRecipient.get(), _typeNotificationRepository.findBy_sName(sType)).get());
        }
    }

    @PatchMapping("/deleteNotificationById/{notificationId}")
    public void deleteNotificatinById(@PathVariable("notificationId") int iNotificationId) {
        Optional<Notification> optionalNotification = _notificationRepository.findById(iNotificationId);
        if(optionalNotification.isPresent()) {
            _notificationRepository.delete(optionalNotification.get());
        }
    }

    @GetMapping("/getUserWarnings/{userId}")
    public List<Notification> getUserWarnings(@PathVariable("userId") int iUserId) {
        Optional<User> optionalUser = _userRepository.findById(iUserId);
        if(optionalUser.isPresent()) {
            return _notificationRepository.findUserWarnings(optionalUser.get());
        } else {
            return Collections.emptyList();
        }
    }

    @GetMapping("/countNewNotifications/{userId}")
    public int countNewNotifications(@PathVariable("userId") int iUserId) {
        Optional<User> optionalUser = _userRepository.findById(iUserId);
        if(optionalUser.isPresent()) {
            return _notificationRepository.countNewNotifications(optionalUser.get());
        } else {
            return -1;
        }
    }

    @PatchMapping("/setSeen/{notificationId}")
    public void setSeen(@PathVariable("notificationId") int iNotificationId) {
        Optional<Notification> optionalNotification = _notificationRepository.findById(iNotificationId);
        if(optionalNotification.isPresent()) {
            optionalNotification.get().set_bSeen(true);
            _notificationRepository.save(optionalNotification.get());
        }
    }
}
