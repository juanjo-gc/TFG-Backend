package es.uca.tfg.backend.repository;

import es.uca.tfg.backend.entity.UserImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserImageRepository extends JpaRepository<UserImage, Integer> {
    UserImage findBy_iId(int iId);

}
