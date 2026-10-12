package ke.co.ssd.lodwar.ticket.controller;

import jakarta.validation.Valid;
import ke.co.ssd.lodwar.ticket.dto.request.CreateTicketRequestDTO;
import ke.co.ssd.lodwar.ticket.dto.request.TicketRequestDTO;
import ke.co.ssd.lodwar.ticket.dto.request.UpdateTicketRequestDTO;
import ke.co.ssd.lodwar.ticket.dto.response.TicketResponseDTO;
import ke.co.ssd.lodwar.ticket.dto.response.TicketStatsResponseDTO;
import ke.co.ssd.lodwar.ticket.service.TicketService;
import ke.co.ssd.lodwar.utils.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = TicketController.ROOT_PATH)
@CrossOrigin
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    static final String ROOT_PATH = "/api/tickets";
    private static final String ID_PATH = "/{id}";
    private static final String STATS_PATH = "/stats";

    private static final String TICKET_CREATED_MESSAGE = "Ticket created successfully.";
    private static final String TICKETS_FETCHED_MESSAGE = "Tickets fetched successfully.";
    private static final String TICKET_UPDATED_MESSAGE = "Ticket updated successfully.";
    private static final String TICKET_STATS_MESSAGE = "Ticket stats fetched successfully.";

    @PostMapping
    public ResponseEntity<ApiResponse<TicketResponseDTO>> createTicket(
            @Valid @RequestBody final CreateTicketRequestDTO createTicketRequestDTO) {

        final TicketResponseDTO ticket = ticketService.create(createTicketRequestDTO);

        return ResponseEntity.ok(ApiResponse.success(ticket, TICKET_CREATED_MESSAGE));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<TicketResponseDTO>>> findAll(
            final TicketRequestDTO ticketRequestDTO,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) final Pageable pageable) {

        final Page<TicketResponseDTO> page = ticketService.findAll(ticketRequestDTO, pageable);

        return ResponseEntity.ok(ApiResponse.pagedResponse(page, TICKETS_FETCHED_MESSAGE));
    }

    @PatchMapping(path = ID_PATH)
    public ResponseEntity<ApiResponse<TicketResponseDTO>> updateTicket(
            @PathVariable final Long id,
            @RequestBody final UpdateTicketRequestDTO updateTicketRequestDTO) {

        final TicketResponseDTO ticket = ticketService.update(id, updateTicketRequestDTO);

        return ResponseEntity.ok(ApiResponse.success(ticket, TICKET_UPDATED_MESSAGE));
    }

    @GetMapping(path = STATS_PATH)
    public ResponseEntity<ApiResponse<TicketStatsResponseDTO>> retrieveStats() {

        final TicketStatsResponseDTO stats = ticketService.retrieveStats();

        return ResponseEntity.ok(ApiResponse.success(stats, TICKET_STATS_MESSAGE));
    }
}
