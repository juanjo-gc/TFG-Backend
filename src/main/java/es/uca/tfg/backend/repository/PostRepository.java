package es.uca.tfg.backend.repository;

import es.uca.tfg.backend.entity.Post;
import es.uca.tfg.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Integer> {
    List<Post> findBy_user(User user);
}
