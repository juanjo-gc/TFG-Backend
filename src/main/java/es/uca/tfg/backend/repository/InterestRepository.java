package es.uca.tfg.backend.repository;

import es.uca.tfg.backend.entity.Interest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InterestRepository extends JpaRepository<Interest, Integer> {
    Interest findBy_sName(String sName);
}
