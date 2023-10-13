package es.uca.tfg.backend.repository;

import es.uca.tfg.backend.entity.Location;
import es.uca.tfg.backend.entity.Province;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LocationRepository extends JpaRepository<Location, Integer> {
    Optional<Location> findBy_sName(String sName);

    List<Location> findBy_province(Province province);
}
