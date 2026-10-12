package ke.co.ssd.lodwar.ticket.service;

import ke.co.ssd.lodwar.ticket.dto.request.CreateTicketRequestDTO;
import ke.co.ssd.lodwar.ticket.dto.request.TicketRequestDTO;
import ke.co.ssd.lodwar.ticket.dto.request.UpdateTicketRequestDTO;
import ke.co.ssd.lodwar.ticket.dto.response.TicketResponseDTO;
import ke.co.ssd.lodwar.ticket.dto.response.TicketStatsResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TicketService {

    TicketResponseDTO create(final CreateTicketRequestDTO createTicketRequestDTO);

    Page<TicketResponseDTO> findAll(final TicketRequestDTO ticketRequestDTO, final Pageable pageable);

    TicketResponseDTO update(final Long id, final UpdateTicketRequestDTO updateTicketRequestDTO);

    TicketStatsResponseDTO retrieveStats();
}
