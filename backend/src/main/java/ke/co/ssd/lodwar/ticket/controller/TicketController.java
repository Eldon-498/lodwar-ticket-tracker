package ke.co.ssd.lodwar.ticket.controller;

import jakarta.validation.Valid;
import ke.co.ssd.lodwar.ticket.dto.request.CreateTicketRequestDTO;
import ke.co.ssd.lodwar.ticket.dto.request.TicketRequestDTO;
import ke.co.ssd.lodwar.ticket.dto.request.UpdateTicketRequestDTO;
import ke.co.ssd.lodwar.ticket.dto.response.TicketResponseDTO;
import ke.co.ssd.lodwar.ticket.dto.response.TicketStatsResponseDTO;
import ke.co.ssd.lodwar.ticket.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = TicketController.ROOT_PATH)
@CrossOrigin
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    static final String ROOT_PATH = "/api/tickets";
    private static final String ID_PATH = "/{id}";
    private static final String STATS_PATH = "/stats";

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TicketResponseDTO createTicket(@Valid @RequestBody final CreateTicketRequestDTO createTicketRequestDTO) {

        return ticketService.create(createTicketRequestDTO);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<TicketResponseDTO> findAll(final TicketRequestDTO ticketRequestDTO,
                                            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) final Pageable pageable) {

        return ticketService.findAll(ticketRequestDTO, pageable);
    }

    @PatchMapping(path = ID_PATH)
    @ResponseStatus(HttpStatus.OK)
    public TicketResponseDTO updateTicket(@PathVariable final Long id,
                                           @RequestBody final UpdateTicketRequestDTO updateTicketRequestDTO) {

        return ticketService.update(id, updateTicketRequestDTO);
    }

    @GetMapping(path = STATS_PATH)
    @ResponseStatus(HttpStatus.OK)
    public TicketStatsResponseDTO retrieveStats() {

        return ticketService.retrieveStats();
    }
}
