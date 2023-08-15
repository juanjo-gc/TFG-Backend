package es.uca.tfg.backend.repository;

import es.uca.tfg.backend.entity.Comment;
import es.uca.tfg.backend.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
    @Query("SELECT c FROM Comment c WHERE c._event = :event ORDER BY _tDatetime ASC")
    List<Comment> findOrderedCommentsByDatetime(Event event);
}
