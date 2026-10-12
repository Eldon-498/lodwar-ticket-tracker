package ke.co.ssd.lodwar.ticket.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import ke.co.ssd.lodwar.ticket.entity.enums.TicketPriority;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreateTicketRequestDTO {

    @NotBlank(message = "is required")
    @Size(max = 200, message = "must be at most 200 characters")
    private String title;

    @Size(max = 2000, message = "must be at most 2000 characters")
    private String description;

    private TicketPriority priority;
}
