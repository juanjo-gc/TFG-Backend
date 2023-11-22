package es.uca.tfg.backend.repository;

import es.uca.tfg.backend.entity.AboutMeAnswer;
import es.uca.tfg.backend.entity.AboutMeQuestion;
import es.uca.tfg.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AboutMeAnswerRepository extends JpaRepository<AboutMeAnswer, Integer> {

    List<AboutMeAnswer> findBy_question(AboutMeQuestion question);

    List<AboutMeAnswer> findBy_user(User user);

    @Query("SELECT a FROM AboutMeAnswer a WHERE a._user = :user AND a._question = :question")
    Optional<AboutMeAnswer> findByUserAndQuestion(@Param("user") User user, @Param("question") AboutMeQuestion question);
}
