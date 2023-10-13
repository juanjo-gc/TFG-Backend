package es.uca.tfg.backend.repository;

import es.uca.tfg.backend.entity.Notification;
import es.uca.tfg.backend.entity.TypeNotification;
import es.uca.tfg.backend.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface NotificationRepository extends JpaRepository<Notification, Integer> {

    @Query("SELECT n FROM Notification n WHERE n._recipient = :recipient ORDER BY n._tCreatedAt DESC")
    Page<Notification> findUserNotifications(@Param("recipient") User recipient, Pageable pageable);

    @Query("SELECT n FROM Notification n WHERE n._issuer = :issuer AND n._recipient = :recipient AND n._type = :type")
    Optional<Notification> findByIssuerAndRecipientAndType(@Param("issuer") User issuer, @Param("recipient") User recipient, @Param("type") TypeNotification type);

    @Query("SELECT n FROM Notification n WHERE n._recipient = :user AND n._type = 'BehaviorWarning'")
    List<Notification> findUserWarnings(@Param("user") User user);
}
