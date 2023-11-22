package es.uca.tfg.backend.repository;

import es.uca.tfg.backend.entity.FAQ;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FAQRepository extends JpaRepository<FAQ, Integer> {
}
