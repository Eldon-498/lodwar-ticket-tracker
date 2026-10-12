package ke.co.ssd.lodwar.ticket.factory;

import ke.co.ssd.lodwar.ticket.dto.request.CreateTicketRequestDTO;
import ke.co.ssd.lodwar.ticket.entity.Ticket;
import ke.co.ssd.lodwar.ticket.entity.enums.TicketPriority;
import ke.co.ssd.lodwar.ticket.entity.enums.TicketStatus;

import java.util.Objects;

public class TicketFactory {

    private TicketFactory() {}

    public static Ticket create(final CreateTicketRequestDTO request) {

        return Ticket.builder()
            .title(request.getTitle().trim())
            .description(Objects.nonNull(request.getDescription()) ? request.getDescription().trim() : null)
            .status(TicketStatus.OPEN)
            .priority(Objects.nonNull(request.getPriority()) ? request.getPriority() : TicketPriority.MEDIUM)
            .build();
    }
}
