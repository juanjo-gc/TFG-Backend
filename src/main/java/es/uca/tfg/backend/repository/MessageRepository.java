package es.uca.tfg.backend.repository;

import es.uca.tfg.backend.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Integer> {
}
