package es.uca.tfg.backend.repository;

import es.uca.tfg.backend.entity.Operation;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OperationRepository extends JpaRepository<Operation, Integer> {

    //List<Operation> findTop7ByOrderByTimestampAsc();

}
