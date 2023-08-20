package es.uca.tfg.backend.controller;

import es.uca.tfg.backend.entity.Event;
import es.uca.tfg.backend.entity.Notification;
import es.uca.tfg.backend.entity.Post;
import es.uca.tfg.backend.entity.User;
import es.uca.tfg.backend.repository.*;
import es.uca.tfg.backend.rest.NotificationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:8080")
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

    //    public Notification(String sInfo, User recipient, TypeNotification type, User issuer) {
    //    public Notification(String sInfo, User recipient, TypeNotification type, User issuer, Event event) {
    //    public Notification(String sInfo, User recipient, TypeNotification type) {
    //     public Notification(String sInfo, User recipient, TypeNotification type, User issuer, Post post) {

    @PostMapping("/newNotification")
    public Notification createNotification(NotificationDTO notificationDTO) {
        Optional<User> optionalRecipient = _userRepository.findById(notificationDTO.get_iRecipientId());
        Optional<User> optionalIssuer = _userRepository.findById(notificationDTO.get_iIssuerId());
        Optional<Event> optionalEvent = _eventRepository.findById(notificationDTO.get_iEventId());
        Optional<Post> optionalPost = _postRepository.findById(notificationDTO.get_iPostId());

        if (Objects.equals(notificationDTO.get_sType(), "NewFollow")) {
            if (optionalIssuer.isPresent() && optionalRecipient.isPresent()) {
                return _notificationRepository.save(new Notification(notificationDTO.get_sInfo(), optionalRecipient.get(),
                        _typeNotificationRepository.findBy_sName(notificationDTO.get_sType()), optionalIssuer.get()));
            } else {
                return new Notification();
            }
        } else if (Objects.equals(notificationDTO.get_sType(), "NewEventAssistant") ||
                Objects.equals(notificationDTO.get_sType(), "NewEventComment") ||
                Objects.equals(notificationDTO.get_sType(), "NewEventPhoto")) {
            if (optionalIssuer.isPresent() && optionalRecipient.isPresent() && optionalPost.isPresent()) {
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
            if (optionalRecipient.isPresent() && optionalPost.isPresent()) {
                return _notificationRepository.save(new Notification(notificationDTO.get_sInfo(), optionalRecipient.get(),
                        _typeNotificationRepository.findBy_sName(notificationDTO.get_sType())));
            } else {
                return new Notification();
            }

        } else {
            return new Notification();
        }
    }
}
