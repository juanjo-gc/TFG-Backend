package es.uca.tfg.backend.repository;

import es.uca.tfg.backend.entity.Province;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProvinceRepository extends JpaRepository<Province, Integer> {

    Province findBy_sName(String sName);
}
