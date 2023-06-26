package es.uca.tfg.backend.repository;

import es.uca.tfg.backend.entity.Region;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegionRepository extends JpaRepository<Region, Integer> {

    Region findBy_sName(String sName);
}
