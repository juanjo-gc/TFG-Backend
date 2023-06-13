package es.uca.tfg.backend.repository;

import es.uca.tfg.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findBy_sEmail(String sEmail);
    User findBy_iId(int iId);
    User findBy_sUsername(String sUsername);
    List<User> findFirst7By_sUsernameStartsWith(String sUsername);

}
