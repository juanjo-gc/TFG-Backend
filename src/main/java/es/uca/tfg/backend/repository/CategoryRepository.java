package es.uca.tfg.backend.repository;

import es.uca.tfg.backend.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

    Optional<Category> findBy_sName(String sName);
}
