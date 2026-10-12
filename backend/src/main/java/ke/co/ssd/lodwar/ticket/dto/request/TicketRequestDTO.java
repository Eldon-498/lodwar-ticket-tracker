package ke.co.ssd.lodwar.ticket.dto.request;

import ke.co.ssd.lodwar.ticket.entity.enums.TicketStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TicketRequestDTO {

    private TicketStatus status;

    public boolean isFilteredByStatus() {

        return Objects.nonNull(status);
    }
}
