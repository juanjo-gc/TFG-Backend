package es.uca.tfg.backend.repository;

import es.uca.tfg.backend.entity.Message;
import es.uca.tfg.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Integer> {
    //List<Message> findBy_issuerAnd_recipientOrderBy_tSentAtAsc(User issuer, User recipient);
}
