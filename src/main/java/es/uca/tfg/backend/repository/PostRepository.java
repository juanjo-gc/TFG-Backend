package es.uca.tfg.backend.repository;

import es.uca.tfg.backend.entity.Post;
import es.uca.tfg.backend.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface PostRepository extends JpaRepository<Post, Integer> {
    Post findBy_iId(int iId);
    List<Post> findBy_user(User user);

    @Query("SELECT p FROM Post p WHERE p._repliesTo = :post AND p._user._bIsSuspended = FALSE ORDER BY p._tCreatedAt DESC")
    Page<Post> findPostReplies(@Param("post") Post post, Pageable pageable);

    @Query("SELECT p FROM Post p WHERE p._user = :user OR :user MEMBER OF p._user._setFollowers AND p._tDeleteDate = NULL ORDER BY p._tCreatedAt DESC")
    Page<Post> findTimelinePosts(@Param("user") User user, Pageable pageable);


}
