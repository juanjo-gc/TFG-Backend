package es.uca.tfg.backend.repository;

import es.uca.tfg.backend.entity.ImagePath;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImagePathRepository extends JpaRepository<ImagePath, Integer> {
    ImagePath findBy_iId(int iId);

}
