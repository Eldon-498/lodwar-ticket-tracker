package ke.co.ssd.lodwar.ticket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import ke.co.ssd.lodwar.ticket.dto.request.CreateTicketRequestDTO;
import ke.co.ssd.lodwar.ticket.entity.enums.TicketPriority;
import ke.co.ssd.lodwar.ticket.entity.enums.TicketStatus;
import ke.co.ssd.lodwar.ticket.repository.TicketRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Testcontainers
class TicketControllerIntegrationTest {

    @Container
    static final MySQLContainer<?> MYSQL = new MySQLContainer<>("mysql:8.0")
        .withDatabaseName("lodwar_test")
        .withUsername("lodwar")
        .withPassword("lodwar");

    @DynamicPropertySource
    static void overrideDataSource(final DynamicPropertyRegistry registry) {

        registry.add("spring.datasource.url", MYSQL::getJdbcUrl);
        registry.add("spring.datasource.username", MYSQL::getUsername);
        registry.add("spring.datasource.password", MYSQL::getPassword);
        registry.add("spring.datasource.driver-class-name", MYSQL::getDriverClassName);
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TicketRepository ticketRepository;

    @BeforeEach
    void resetState() {

        ticketRepository.deleteAll();
    }

    @Test
    void createTicket_persistsAndReturnsCreated() throws Exception {

        final CreateTicketRequestDTO request = new CreateTicketRequestDTO(
            "Printer offline",
            "Second floor printer not reachable",
            TicketPriority.HIGH);

        mockMvc.perform(post("/api/tickets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id", greaterThan(0)))
            .andExpect(jsonPath("$.title", is("Printer offline")))
            .andExpect(jsonPath("$.status", is(TicketStatus.OPEN.name())))
            .andExpect(jsonPath("$.priority", is(TicketPriority.HIGH.name())));
    }

    @Test
    void createTicket_withBlankTitle_returnsBadRequest() throws Exception {

        final ObjectNode payload = objectMapper.createObjectNode();
        payload.put("title", "");

        mockMvc.perform(post("/api/tickets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload.toString()))
            .andExpect(status().isBadRequest());
    }

    @Test
    void getStats_countsGroupedByStatus() throws Exception {

        createViaApi("First", TicketPriority.LOW);
        createViaApi("Second", TicketPriority.MEDIUM);
        final Long thirdId = createViaApi("Third", TicketPriority.HIGH);

        mockMvc.perform(patch("/api/tickets/" + thirdId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"status\":\"CLOSED\"}"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status", is(TicketStatus.CLOSED.name())));

        mockMvc.perform(get("/api/tickets/stats"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.total", is(3)))
            .andExpect(jsonPath("$.byStatus.OPEN", is(2)))
            .andExpect(jsonPath("$.byStatus.IN_PROGRESS", is(0)))
            .andExpect(jsonPath("$.byStatus.CLOSED", is(1)));
    }

    private Long createViaApi(final String title, final TicketPriority priority) throws Exception {

        final CreateTicketRequestDTO request = new CreateTicketRequestDTO(title, null, priority);

        final String response = mockMvc.perform(post("/api/tickets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andReturn().getResponse().getContentAsString();

        return objectMapper.readTree(response).get("id").asLong();
    }
}
