package es.uca.tfg.backend.controller;

import es.uca.tfg.backend.entity.Comment;
import es.uca.tfg.backend.entity.Event;
import es.uca.tfg.backend.entity.Post;
import es.uca.tfg.backend.entity.User;
import es.uca.tfg.backend.repository.CommentRepository;
import es.uca.tfg.backend.repository.EventRepository;
import es.uca.tfg.backend.repository.UserRepository;
import es.uca.tfg.backend.rest.CommentDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class CommentController {

    @Autowired
    private CommentRepository _commentRepository;
    @Autowired
    private EventRepository _eventrepository;
    @Autowired
    private UserRepository _userRepository;

    @PostMapping("/newComment")
    public Comment newComment(@RequestBody CommentDTO commentDTO) {
        Optional<User> optionalUser = _userRepository.findById(commentDTO.get_iUserId());
        Optional<Event> optionalEvent = _eventrepository.findById(commentDTO.get_iEventId());
        if(optionalUser.isPresent() && optionalEvent.isPresent()) {
            return _commentRepository.save(new Comment(commentDTO.get_sText(), optionalUser.get(), optionalEvent.get()));
        } else {
            return new Comment();
        }
    }

    @GetMapping("/getEventComments/{eventId}")
    public List<Comment> getEventComments(@PathVariable("eventId") int iEventId) {
        Optional<Event> optionalEvent = _eventrepository.findById(iEventId);
        if(optionalEvent.isPresent()) {
            return _commentRepository.findOrderedCommentsByDatetime(optionalEvent.get());
        } else {
            return Collections.emptyList();
        }
    }

    @PatchMapping("softDeleteOrRestoreComment/{commentId}")
    public boolean softDeleteOrRestoreComment(@PathVariable("commentId") int iCommentId) {
        Optional<Comment> optionalComment = _commentRepository.findById(iCommentId);
        if(optionalComment.isPresent()) {
            if(Objects.equals(optionalComment.get().get_tDeleteDate(), null))
                optionalComment.get().set_tDeleteDate(LocalDateTime.now());
            else
                optionalComment.get().set_tDeleteDate(null);
            return _commentRepository.save(optionalComment.get()).get_tDeleteDate() != null;
        } else {
            return false;
        }
    }
}
