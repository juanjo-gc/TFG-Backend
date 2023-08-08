package es.uca.tfg.backend.repository;

import es.uca.tfg.backend.entity.Province;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProvinceRepository extends JpaRepository<Province, Integer> {

    Province findBy_sName(String sName);
    @Query("SELECT p._sName FROM Province p")
    List<String> findProvinceNames();
}
