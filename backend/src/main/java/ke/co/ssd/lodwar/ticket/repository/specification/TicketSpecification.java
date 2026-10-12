package ke.co.ssd.lodwar.ticket.repository.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import ke.co.ssd.lodwar.ticket.dto.request.TicketRequestDTO;
import ke.co.ssd.lodwar.ticket.entity.Ticket;
import ke.co.ssd.lodwar.utils.SpecificationUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.data.jpa.domain.Specification;

import java.util.Optional;

@Builder
@AllArgsConstructor
public class TicketSpecification implements Specification<Ticket> {

    private static final String STATUS = "status";

    private final TicketRequestDTO ticketRequestDTO;

    @Override
    public Predicate toPredicate(final Root<Ticket> root, final CriteriaQuery<?> query, final CriteriaBuilder criteriaBuilder) {

        return SpecificationUtils.toPredicateConditionAND(
            criteriaBuilder,
            buildSearchByStatus(root, criteriaBuilder)
        );
    }

    private Optional<Predicate> buildSearchByStatus(final Root<Ticket> root, final CriteriaBuilder criteriaBuilder) {

        return ticketRequestDTO.isFilteredByStatus()
            ? Optional.of(criteriaBuilder.equal(root.get(STATUS), ticketRequestDTO.getStatus()))
            : Optional.empty();
    }
}
