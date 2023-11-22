package es.uca.tfg.backend.repository;

import es.uca.tfg.backend.entity.Province;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProvinceRepository extends JpaRepository<Province, Integer> {

    Province findBy_sName(String sName);

    @Query("SELECT p FROM Province p WHERE :name = p._sName")
    Optional<Province> findByName(@Param("name") String sName);

    @Query("SELECT p._sName FROM Province p")
    List<String> findProvinceNames();

}
