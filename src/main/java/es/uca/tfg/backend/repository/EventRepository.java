package es.uca.tfg.backend.repository;

import es.uca.tfg.backend.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Integer> {
    @Query("SELECT u.id FROM Event e, User u WHERE e.id = :eventId AND u MEMBER OF e._setAssistants")
    List<Integer> findAssistantIds(@Param("eventId") int eventId);
}
