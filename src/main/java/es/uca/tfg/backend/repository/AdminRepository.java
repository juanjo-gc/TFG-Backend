package es.uca.tfg.backend.repository;

import es.uca.tfg.backend.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AdminRepository extends JpaRepository<Admin, Integer> {

    @Query("SELECT a.id FROM Admin a")
    List<Integer> findAllAdminIds();
}
