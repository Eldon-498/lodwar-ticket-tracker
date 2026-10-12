package ke.co.ssd.lodwar.ticket.dto.response;

import ke.co.ssd.lodwar.ticket.entity.Ticket;
import ke.co.ssd.lodwar.ticket.entity.enums.TicketPriority;
import ke.co.ssd.lodwar.ticket.entity.enums.TicketStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Objects;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TicketResponseDTO {

    private Long id;
    private String title;
    private String description;
    private TicketStatus status;
    private TicketPriority priority;
    private Instant createdAt;
    private Instant updatedAt;

    public static TicketResponseDTO from(final Ticket ticket) {

        if (Objects.isNull(ticket)) {

            return null;
        }

        return TicketResponseDTO.builder()
            .id(ticket.getId())
            .title(ticket.getTitle())
            .description(ticket.getDescription())
            .status(ticket.getStatus())
            .priority(ticket.getPriority())
            .createdAt(ticket.getCreatedAt())
            .updatedAt(ticket.getUpdatedAt())
            .build();
    }
}
