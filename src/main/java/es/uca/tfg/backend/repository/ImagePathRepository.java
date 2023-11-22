package es.uca.tfg.backend.repository;

import es.uca.tfg.backend.entity.ImagePath;
import org.springframework.data.jpa.repository.JpaRepository;

import java.awt.*;
import java.util.Optional;

public interface ImagePathRepository extends JpaRepository<ImagePath, Integer> {
    ImagePath findBy_iId(int iId);
    Optional<ImagePath> findBy_sName(String sName);
}
