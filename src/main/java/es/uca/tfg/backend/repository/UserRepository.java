package es.uca.tfg.backend.repository;

import es.uca.tfg.backend.entity.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findBy_sEmail(String sEmail);
    User findBy_iId(int iId);
    User findBy_sUsername(String sUsername);
    List<User> findFirst7By_sUsernameStartsWith(String sUsername);
    List<User> findBy_sUsernameStartsWith(String sUsername);
    @Query("SELECT u.id FROM User u")
    List<Integer> findAllIds();

    @Query("SELECT u.id FROM User u WHERE ( :interest1 IS NULL OR :interest1 MEMBER OF u._setInterests ) AND" +
            "( :interest2 IS NULL OR :interest2 MEMBER OF u._setInterests ) AND" +
            "( :interest3 IS NULL OR :interest3 MEMBER OF u._setInterests )")
    List<Integer> findUserIdsByOptionalInterests(@Param("interest1") Interest interest1, @Param("interest2") Interest interest2, @Param("interest3") Interest interest3);


    @Query("SELECT DISTINCT u.id FROM User u, Region r, Country c WHERE ( :province IS NULL OR :province = u._province ) AND" +
            "( :region IS NULL OR u._province._region = :region)  AND " +
            "( :country IS NULL OR u._province._region._country = :country )" )
    List<Integer> findUserIdsByLocation(@Param("province") Province province, @Param("region") Region region, @Param("country") Country country);

    @Query("SELECT u.id FROM User u WHERE ( :user MEMBER OF u._setFollowers )")
    List<Integer> findFollowingUserIds(@Param("user") User user);

    @Query("SELECT DISTINCT u.id FROM User u, Region r, Country c WHERE ( :province IS NULL OR :province = u._province ) AND" +
            "( :region IS NULL OR u._province._region = :region)  AND " +
            "( :country IS NULL OR u._province._region._country = :country ) AND" +
            "( :interest1 IS NULL OR :interest1 MEMBER OF u._setInterests ) AND" +
            "( :interest2 IS NULL OR :interest2 MEMBER OF u._setInterests ) AND" +
            "( :interest3 IS NULL OR :interest3 MEMBER OF u._setInterests ) AND" +
            "( :userId != u.id )")
    Page<Integer> findFilteredUsers(@Param("province") Province province, @Param("region") Region region, @Param("country") Country country,
                                 @Param("interest1") Interest interest1, @Param("interest2") Interest interest2, @Param("interest3") Interest interest3,
                                 @Param("userId") int iUserId,
                                 Pageable pageable);
}
