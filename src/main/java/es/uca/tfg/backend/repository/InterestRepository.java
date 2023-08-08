package es.uca.tfg.backend.repository;

import es.uca.tfg.backend.entity.Interest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface InterestRepository extends JpaRepository<Interest, Integer> {
    Interest findBy_sName(String sName);
    @Query("SELECT i._sName FROM Interest i")
    List<String> findInterestNames();
}
