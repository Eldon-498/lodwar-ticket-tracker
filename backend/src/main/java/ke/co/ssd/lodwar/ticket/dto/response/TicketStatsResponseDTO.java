package ke.co.ssd.lodwar.ticket.dto.response;

import ke.co.ssd.lodwar.ticket.entity.enums.TicketStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TicketStatsResponseDTO {

    private Long total;

    private Map<TicketStatus, Long> byStatus;

    public static TicketStatsResponseDTO from(final List<Object[]> grouped) {

        final Map<TicketStatus, Long> counts = new EnumMap<>(TicketStatus.class);

        for (final TicketStatus status : TicketStatus.values()) {

            counts.put(status, 0L);
        }

        long total = 0L;

        for (final Object[] row : grouped) {

            final TicketStatus status = (TicketStatus) row[0];
            final Long count = ((Number) row[1]).longValue();
            counts.put(status, count);
            total += count;
        }

        return TicketStatsResponseDTO.builder()
            .total(total)
            .byStatus(counts)
            .build();
    }
}
