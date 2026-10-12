package ke.co.ssd.lodwar.ticket.dto.request;

import ke.co.ssd.lodwar.ticket.entity.enums.TicketPriority;
import ke.co.ssd.lodwar.ticket.entity.enums.TicketStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateTicketRequestDTO {

    private TicketStatus status;

    private TicketPriority priority;

    public boolean hasStatus() {

        return Objects.nonNull(status);
    }

    public boolean hasPriority() {

        return Objects.nonNull(priority);
    }
}
