package es.uca.tfg.backend.repository;

import es.uca.tfg.backend.entity.Reply;
import es.uca.tfg.backend.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReplyRepository extends JpaRepository<Reply, Integer> {

    @Query("SELECT r FROM Reply r WHERE r._ticket = :ticket ORDER BY r._tCreationDate ASC")
    List<Reply> findByTicketOrderByCreatedAtAsc(@Param("ticket") Ticket ticket);
}
