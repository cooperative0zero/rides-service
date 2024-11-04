package com.modsen.software.rides.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.modsen.software.rides.client.PassengerServiceClient;
import com.modsen.software.rides.dto.PassengerResponse;
import com.modsen.software.rides.dto.RideRequest;
import com.modsen.software.rides.entity.Ride;
import com.modsen.software.rides.entity.enumeration.RideStatus;
import com.modsen.software.rides.repository.RideRepository;
import jakarta.annotation.PostConstruct;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;

import java.time.OffsetDateTime;
import java.util.List;

import static com.modsen.software.rides.util.Constants.*;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class RidesIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private RideRepository rideRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @MockBean
    private PassengerServiceClient passengerServiceClient;

    @PostConstruct
    void setupServiceClientsMocks() {
        when(passengerServiceClient.getById(1L)).thenReturn(passengerResponse);
        when(passengerServiceClient.getById(2L)).thenReturn(anotherPassengerResponse);
    }

    @BeforeEach
    void prepare() {
        jdbcTemplate.execute("TRUNCATE TABLE rides RESTART IDENTITY;");
    }

    @SneakyThrows
    @Test
    void getAllRidesWhenRidesExist() {
        rideRepository.saveAll(List.of(ride, anotherRide));

        mockMvc.perform(get("/api/v1/rides")
                        .param("pageNumber", "0")
                        .param("pageSize", "10")
                        .param("sortBy", "id")
                        .param("sortOrder", "asc")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items[0].id", is(ride.getId().intValue())))
                .andExpect(jsonPath("$.items[1].id", is(anotherRide.getId().intValue())))
                .andExpect(jsonPath("$.items[0].passengerId", is(ride.getPassengerId().intValue())))
                .andExpect(jsonPath("$.items[1].passengerId", is(anotherRide.getPassengerId().intValue())))
                .andExpect(jsonPath("$.items[0].driverId", is(ride.getDriverId().intValue())))
                .andExpect(jsonPath("$.items[1].driverId", is(anotherRide.getDriverId().intValue())))
                .andExpect(jsonPath("$.total", is(2)))
                .andExpect(jsonPath("$.page", is(0)))
                .andExpect(jsonPath("$.size", is(10)));
    }

    @SneakyThrows
    @Test
    void getRideByIdWhenRideExists() {
        rideRepository.save(ride);

        mockMvc.perform(get("/api/v1/rides/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(ride.getId().intValue())))
                .andExpect(jsonPath("$.passengerId", is(ride.getPassengerId().intValue())))
                .andExpect(jsonPath("$.driverId", is(ride.getDriverId().intValue())));
    }

    @SneakyThrows
    @Test
    void getAllByPassengerIdWhenRidesExist() {
        rideRepository.saveAll(List.of(ride, anotherRide));

        mockMvc.perform(get("/api/v1/rides/passengers/{id}", 1L)
                        .param("pageNumber", "0")
                        .param("pageSize", "10")
                        .param("sortBy", "id")
                        .param("sortOrder", "asc")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items[0].id", is(ride.getId().intValue())))
                .andExpect(jsonPath("$.items[0].passengerId", is(ride.getPassengerId().intValue())))
                .andExpect(jsonPath("$.items[0].driverId", is(ride.getDriverId().intValue())))
                .andExpect(jsonPath("$.total", is(1)))
                .andExpect(jsonPath("$.page", is(0)))
                .andExpect(jsonPath("$.size", is(10)));
    }

    @SneakyThrows
    @Test
    void getAllByDriverIdWhenRidesExist() {
        rideRepository.saveAll(List.of(ride, anotherRide));

        mockMvc.perform(get("/api/v1/rides/drivers/{id}", 2L)
                        .param("pageNumber", "0")
                        .param("pageSize", "10")
                        .param("sortBy", "id")
                        .param("sortOrder", "asc")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items[0].id", is(anotherRide.getId().intValue())))
                .andExpect(jsonPath("$.items[0].passengerId", is(anotherRide.getPassengerId().intValue())))
                .andExpect(jsonPath("$.items[0].driverId", is(anotherRide.getDriverId().intValue())))
                .andExpect(jsonPath("$.total", is(1)))
                .andExpect(jsonPath("$.page", is(0)))
                .andExpect(jsonPath("$.size", is(10)));
    }

    @SneakyThrows
    @Test
    void saveRideWhenRideNotExists() {
        mockMvc.perform(post("/api/v1/rides")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(rideRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(ride.getId().intValue())))
                .andExpect(jsonPath("$.passengerId", is(ride.getPassengerId().intValue())))
                .andExpect(jsonPath("$.destinationAddress", is(ride.getDestinationAddress())))
                .andExpect(jsonPath("$.departureAddress", is(ride.getDepartureAddress())));
    }

    @SneakyThrows
    @Test
    void updateRideWhenRideExists() {
        OffsetDateTime now = OffsetDateTime.now();

        RideRequest rideRequestToUpdate = rideRequest.toBuilder()
                .departureAddress("New Departure address 1")
                .destinationAddress("New Destination address 1")
                .creationDate(now)
                .build();

        rideRepository.save(ride);
        mockMvc.perform(put("/api/v1/rides")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(rideRequestToUpdate)))
                .andExpect(jsonPath("$.id", is(rideRequestToUpdate.getId().intValue())))
                .andExpect(jsonPath("$.passengerId", is(rideRequestToUpdate.getPassengerId().intValue())))
                .andExpect(jsonPath("$.departureAddress", is(rideRequestToUpdate.getDepartureAddress())))
                .andExpect(jsonPath("$.destinationAddress", is(rideRequestToUpdate.getDestinationAddress())));
    }

    @SneakyThrows
    @Test
    void changeRideStatusWhenRideExists() {
        rideRepository.save(ride);

        mockMvc.perform(patch("/api/v1/rides/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("userId", "1")
                        .param("status", "TO_PASSENGER"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(ride.getId().intValue())))
                .andExpect(jsonPath("$.passengerId", is(ride.getPassengerId().intValue())))
                .andExpect(jsonPath("$.driverId", is(ride.getDriverId().intValue())))
                .andExpect(jsonPath("$.destinationAddress", is(ride.getDestinationAddress())))
                .andExpect(jsonPath("$.departureAddress", is(ride.getDepartureAddress())));
    }
}
