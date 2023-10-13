package es.uca.tfg.backend.repository;

import es.uca.tfg.backend.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CountryRepository extends JpaRepository<Country, Integer> {

    Optional<Country> findBy_sName(String sName);
    @Query("SELECT c._sName FROM Country c")
    List<String> findCountryNames();
}
