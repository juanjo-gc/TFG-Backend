package es.uca.tfg.backend.repository;

import es.uca.tfg.backend.entity.Admin;
import es.uca.tfg.backend.entity.Category;
import es.uca.tfg.backend.entity.Ticket;
import es.uca.tfg.backend.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Integer> {

    List<Ticket> findBy_category(Category category);

    @Query("SELECT t FROM Ticket t WHERE _reported = :reported AND _category._sName = 'Denunciar un usuario'")
    List<Ticket> findUserReports(@Param("reported") User reported);

    List<Ticket> findBy_admin(Admin admin);

    List<Ticket> findBy_issuer(User issuer);
}
