package ke.co.ssd.lodwar.ticket.repository;

import ke.co.ssd.lodwar.ticket.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long>, JpaSpecificationExecutor<Ticket> {

    @Query("SELECT t.status, COUNT(t) FROM Ticket t GROUP BY t.status")
    List<Object[]> countGroupedByStatus();
}
