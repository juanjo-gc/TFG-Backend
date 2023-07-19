package es.uca.tfg.backend.repository;

import es.uca.tfg.backend.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LocationRepository extends JpaRepository<Location, Integer> {
    Optional<Location> findBy_sName(String sName);
}
