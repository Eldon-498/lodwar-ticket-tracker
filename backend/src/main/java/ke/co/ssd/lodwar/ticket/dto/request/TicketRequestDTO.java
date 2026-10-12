package ke.co.ssd.lodwar.ticket.dto.request;

import ke.co.ssd.lodwar.ticket.entity.enums.TicketStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TicketRequestDTO {

    private TicketStatus status;

    public boolean isFilteredByStatus() {

        return Objects.nonNull(status);
    }
}
