package ke.co.ssd.lodwar.ticket.service.impl;

import com.google.common.base.Preconditions;
import ke.co.ssd.lodwar.exceptions.EntityNotFoundException;
import ke.co.ssd.lodwar.exceptions.UnprocessableEntityException;
import ke.co.ssd.lodwar.exceptions.response.ErrorResponseEnum;
import ke.co.ssd.lodwar.ticket.dto.request.CreateTicketRequestDTO;
import ke.co.ssd.lodwar.ticket.dto.request.TicketRequestDTO;
import ke.co.ssd.lodwar.ticket.dto.request.UpdateTicketRequestDTO;
import ke.co.ssd.lodwar.ticket.dto.response.TicketResponseDTO;
import ke.co.ssd.lodwar.ticket.dto.response.TicketStatsResponseDTO;
import ke.co.ssd.lodwar.ticket.entity.Ticket;
import ke.co.ssd.lodwar.ticket.factory.TicketFactory;
import ke.co.ssd.lodwar.ticket.repository.TicketRepository;
import ke.co.ssd.lodwar.ticket.repository.specification.TicketSpecification;
import ke.co.ssd.lodwar.ticket.service.TicketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {

    private static final String ERROR_CREATE_REQUEST_NULL = "Ticket creation request must not be null.";
    private static final String ERROR_UPDATE_REQUEST_NULL = "Ticket update request must not be null.";
    private static final String ERROR_UPDATE_NO_FIELDS = "At least one of status or priority must be provided.";
    private static final String ERROR_TICKET_NOT_FOUND = "Ticket with id [{0}] not found.";

    private final TicketRepository ticketRepository;

    @Override
    @Transactional
    public TicketResponseDTO create(final CreateTicketRequestDTO createTicketRequestDTO) {

        Preconditions.checkArgument(Objects.nonNull(createTicketRequestDTO), ERROR_CREATE_REQUEST_NULL);

        final Ticket ticket = TicketFactory.create(createTicketRequestDTO);

        final Ticket saved = ticketRepository.save(ticket);

        log.info("Created ticket id={} title={}", saved.getId(), saved.getTitle());

        return TicketResponseDTO.from(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TicketResponseDTO> findAll(final TicketRequestDTO ticketRequestDTO, final Pageable pageable) {

        final TicketRequestDTO filter = Objects.nonNull(ticketRequestDTO) ? ticketRequestDTO : new TicketRequestDTO();

        return ticketRepository.findAll(new TicketSpecification(filter), pageable)
            .map(TicketResponseDTO::from);
    }

    @Override
    @Transactional
    public TicketResponseDTO update(final Long id, final UpdateTicketRequestDTO updateTicketRequestDTO) {

        Preconditions.checkArgument(Objects.nonNull(updateTicketRequestDTO), ERROR_UPDATE_REQUEST_NULL);

        if (!updateTicketRequestDTO.hasStatus() && !updateTicketRequestDTO.hasPriority()) {

            throw new UnprocessableEntityException(ERROR_UPDATE_NO_FIELDS, ErrorResponseEnum.TICKET_UPDATE_NO_FIELDS);
        }

        final Ticket ticket = findById(id);

        if (updateTicketRequestDTO.hasStatus()) {

            ticket.setStatus(updateTicketRequestDTO.getStatus());
        }

        if (updateTicketRequestDTO.hasPriority()) {

            ticket.setPriority(updateTicketRequestDTO.getPriority());
        }

        final Ticket saved = ticketRepository.save(ticket);

        log.info("Updated ticket id={} status={} priority={}", saved.getId(), saved.getStatus(), saved.getPriority());

        return TicketResponseDTO.from(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public TicketStatsResponseDTO retrieveStats() {

        return TicketStatsResponseDTO.from(ticketRepository.countGroupedByStatus());
    }

    private Ticket findById(final Long id) {

        return ticketRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException(
                MessageFormat.format(ERROR_TICKET_NOT_FOUND, id),
                ErrorResponseEnum.TICKET_NOT_FOUND));
    }
}
