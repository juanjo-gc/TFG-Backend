package es.uca.tfg.backend.repository;

import es.uca.tfg.backend.entity.Post;
import es.uca.tfg.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface PostRepository extends JpaRepository<Post, Integer> {
    Post findBy_iId(int iId);
    List<Post> findBy_user(User user);

    @Query("SELECT p FROM Post p WHERE p._repliesTo = :post ORDER BY p._tCreatedAt DESC")
    List<Post> findPostReplies(@Param("post") Post post);


}
