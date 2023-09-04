package es.uca.tfg.backend.repository;

import es.uca.tfg.backend.entity.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Integer> {
    @Query("SELECT u.id FROM Event e, User u WHERE e.id = :eventId AND u MEMBER OF e._setAssistants")
    List<Integer> findAssistantIds(@Param("eventId") int eventId);

    @Query("SELECT e FROM Event e WHERE :user MEMBER OF e._setAssistants")
    List<Event> findEventsAssistedByUser(@Param("user") User user);

    @Query("SELECT e.id FROM Event e")
    List<Integer> findAllEventIds();

    List<Integer> findBy_sTitleLike(@Param("title") String sTitle);

    @Query("SELECT e.id FROM Event e WHERE ( :interest1 IS NULL OR :interest1 MEMBER OF e._setInterests ) AND" +
            "( :interest2 IS NULL OR :interest2 MEMBER OF e._setInterests ) AND" +
            "( :interest3 IS NULL OR :interest3 MEMBER OF e._setInterests )")
    List<Integer> findEventIdsByOptionalInterests(@Param("interest1") Interest interest1, @Param("interest2") Interest interest2, @Param("interest3") Interest interest3);

    @Query("SELECT e.id FROM Event e, Region r, Country c WHERE ( :province IS NULL OR :province = e._location._province ) AND" +
            "( :region IS NULL OR e._location._province._region = :region)  AND " +
            "( :country IS NULL OR e._location._province._region._country = :country )" )
    List<Integer> findEventIdsByLocation(@Param("province") Province province, @Param("region") Region region, @Param("country") Country country);


    @Query("SELECT DISTINCT e FROM Event e WHERE ( :province IS NULL OR :province = e._location._province ) AND" +
            "( :region IS NULL OR e._location._province._region = :region)  AND" +
            "( :country IS NULL OR e._location._province._region._country = :country ) AND" +
            "( :interest1 IS NULL OR :interest1 MEMBER OF e._setInterests ) AND " +
            "( :interest2 IS NULL OR :interest2 MEMBER OF e._setInterests ) AND " +
            "( :interest3 IS NULL OR :interest3 MEMBER OF e._setInterests ) AND" +
            "( :date <= e._tCelebratedAt  )")
    Page<Event> findEventIdsByFilters(@Param("province") Province province, @Param("region") Region region, @Param("country") Country country,
                                      @Param("interest1") Interest interest1, @Param("interest2") Interest interest2, @Param("interest3") Interest interest3,
                                      @Param("date") LocalDate tDate, Pageable pageable);

    @Query("SELECT e FROM Event e WHERE e._bIsOnline = true")
    List<Event> findOnlineEvents();

    @Query("SELECT e FROM Event e WHERE e._bIsOnline = TRUE AND" +
            "( :interest1 IS NULL OR :interest1 MEMBER OF e._setInterests ) AND " +
            "( :interest2 IS NULL OR :interest2 MEMBER OF e._setInterests ) AND " +
            "( :interest3 IS NULL OR :interest3 MEMBER OF e._setInterests ) AND" +
            "( CURRENT_DATE <= e._tCelebratedAt  )")
    Page<Event> findOnlineEventIdsByFilter(@Param("interest1") Interest interest1, @Param("interest2") Interest interest2, @Param("interest3") Interest interest3,
                                           Pageable pageable);
    @Query("SELECT e FROM Event e WHERE e._tCelebratedAt >= :localdate")
    List<Event> findEventsBeforeDate(@Param("localdate")  LocalDate localDate);

}
