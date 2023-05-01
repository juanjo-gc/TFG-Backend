package es.uca.tfg.backend.repository;

import es.uca.tfg.backend.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, Integer> {
    Person findBy_iId(int iId);
    long countBy_sUsername(String sUsername);
    long countBy_sEmail(String sEmail);
    Person findBy_sEmail(String sEmail);

}
