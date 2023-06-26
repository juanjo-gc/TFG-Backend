package es.uca.tfg.backend.repository;

import es.uca.tfg.backend.entity.Message;
import es.uca.tfg.backend.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MessageRepository extends JpaRepository<Message, Integer> {
    @Query("SELECT m FROM Message m WHERE (m._issuer = :issuer AND m._recipient = :recipient) OR (m._issuer = :recipient AND m._recipient = :issuer) ORDER BY m._tSentAt ASC")
    List<Message> findConversation(@Param("issuer") User issuer, @Param("recipient") User recipient);

    @Query("SELECT m FROM Message m WHERE (m._issuer = :issuer AND m._recipient = :recipient) OR (m._issuer = :recipient AND m._recipient = :issuer) ORDER BY m._tSentAt DESC")
    Page<Message> findLastIssuerRecipientMessage(@Param("issuer") User issuer, @Param("recipient") User recipient, Pageable pageable);

    @Query("SELECT m._recipient FROM Message m WHERE m._issuer = :user GROUP BY m._recipient")
    List<User> findMessagedUsers(@Param("user") User user);

    @Query("SELECT m._issuer FROM Message m WHERE m._recipient = :user GROUP BY m._issuer")
    List<User> findUserWhoMessagedCurrentUser(@Param("user") User user);






}
