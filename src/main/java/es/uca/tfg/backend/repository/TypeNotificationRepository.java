package es.uca.tfg.backend.repository;

import es.uca.tfg.backend.entity.TypeNotification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TypeNotificationRepository extends JpaRepository<TypeNotification, Integer> {
    TypeNotification findBy_sName(String sName);
}
