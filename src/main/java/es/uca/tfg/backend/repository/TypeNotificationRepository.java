package es.uca.tfg.backend.repository;

import es.uca.tfg.backend.entity.TypeNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TypeNotificationRepository extends JpaRepository<TypeNotification, Integer> {
    TypeNotification findBy_sName(String sName);
}
