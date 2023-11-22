package es.uca.tfg.backend.repository;

import es.uca.tfg.backend.entity.AboutMeQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AboutMeQuestionRepository extends JpaRepository<AboutMeQuestion, Integer> {
}
