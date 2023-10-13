package es.uca.tfg.backend.repository;

import es.uca.tfg.backend.entity.Region;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RegionRepository extends JpaRepository<Region, Integer> {

    Optional<Region> findBy_sName(String sName);
}
