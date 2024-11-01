package com.modsen.software.rides.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.modsen.software.rides.client.PassengerServiceClient;
import com.modsen.software.rides.dto.PaginatedResponse;
import com.modsen.software.rides.dto.PassengerResponse;
import com.modsen.software.rides.dto.RideRequest;
import com.modsen.software.rides.dto.RideResponse;
import com.modsen.software.rides.entity.Ride;
import com.modsen.software.rides.entity.enumeration.RideStatus;
import com.modsen.software.rides.service.RideServiceImpl;
import jakarta.annotation.PostConstruct;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.OffsetDateTime;
import java.util.List;

import static com.modsen.software.rides.util.Constants.*;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RideController.class)
public class RidesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PassengerServiceClient passengerServiceClient;

    @MockBean
    private RideServiceImpl rideService;

    private PaginatedResponse<RideResponse> paginatedResponse;
    private List<RideResponse> rideResponses;
    private List<Ride> rides;

    @PostConstruct
    void setupServiceClientsMocks() {
        when(passengerServiceClient.getById(1L)).thenReturn(passengerResponse);
        when(passengerServiceClient.getById(2L)).thenReturn(anotherPassengerResponse);
    }

    {
        rides = List.of(ride, anotherRide);
        rideResponses = List.of(rideResponse, anotherRideResponse);

        paginatedResponse = new PaginatedResponse<>(
                rideResponses,
                0,
                10,
                rides.size()
        );
    }

    @SneakyThrows
    @Test
    void getAllWhenRidesExist() {
        when(rideService.getAll(any(), any(), any(), any()))
                .thenReturn(rides);

        mockMvc.perform(get("/api/v1/rides")
                        .param("pageNumber", "0")
                        .param("pageSize", "10")
                        .param("sortBy", "id")
                        .param("sortOrder", "asc")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items[0].id", is(rideResponses.get(0).getId().intValue())))
                .andExpect(jsonPath("$.items[1].id", is(rideResponses.get(1).getId().intValue())))
                .andExpect(jsonPath("$.items[0].passengerId", is(rideResponses.get(0).getPassengerId().intValue())))
                .andExpect(jsonPath("$.items[1].passengerId", is(rideResponses.get(1).getPassengerId().intValue())))
                .andExpect(jsonPath("$.items[0].driverId", is(rideResponses.get(0).getDriverId().intValue())))
                .andExpect(jsonPath("$.items[1].driverId", is(rideResponses.get(1).getDriverId().intValue())))
                .andExpect(jsonPath("$.total", is(paginatedResponse.getTotal())))
                .andExpect(jsonPath("$.page", is(paginatedResponse.getPage())))
                .andExpect(jsonPath("$.size", is(paginatedResponse.getSize())));

        verify(rideService, times(1)).getAll(any(), any(), any(), any());
    }

    @SneakyThrows
    @Test
    void getAllByPassengerIdWhenRidesExist() {
        when(rideService.getAllByPassengerId(argThat(i -> i.compareTo(1L) == 0), any(), any(), any(), any()))
                .thenReturn(rides.stream().filter(rating -> rating.getPassengerId().compareTo(1L) == 0).toList());

        mockMvc.perform(get("/api/v1/rides/passengers/{id}", 1L)
                        .param("pageNumber", "0")
                        .param("pageSize", "10")
                        .param("sortBy", "id")
                        .param("sortOrder", "asc")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items[0].id", is(rideResponses.get(0).getId().intValue())))
                .andExpect(jsonPath("$.items[0].passengerId", is(rideResponses.get(0).getPassengerId().intValue())))
                .andExpect(jsonPath("$.items[0].driverId", is(rideResponses.get(0).getDriverId().intValue())))
                .andExpect(jsonPath("$.total", is(1)))
                .andExpect(jsonPath("$.page", is(paginatedResponse.getPage())))
                .andExpect(jsonPath("$.size", is(paginatedResponse.getSize())));

        verify(rideService, times(1)).getAllByPassengerId(argThat(i -> i.compareTo(1L) == 0), any(), any(), any(), any());
    }

    @SneakyThrows
    @Test
    void getAllByDriverIdWhenRidesExist() {
        when(rideService.getAllByDriverId(argThat(i -> i.compareTo(2L) == 0), any(), any(), any(), any()))
                .thenReturn(rides.stream().filter(rating -> rating.getDriverId().compareTo(2L) == 0).toList());

        mockMvc.perform(get("/api/v1/rides/drivers/{id}", 2L)
                        .param("pageNumber", "0")
                        .param("pageSize", "10")
                        .param("sortBy", "id")
                        .param("sortOrder", "asc")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items[0].id", is(rideResponses.get(1).getId().intValue())))
                .andExpect(jsonPath("$.items[0].passengerId", is(rideResponses.get(1).getPassengerId().intValue())))
                .andExpect(jsonPath("$.items[0].driverId", is(rideResponses.get(1).getDriverId().intValue())))
                .andExpect(jsonPath("$.total", is(1)))
                .andExpect(jsonPath("$.page", is(paginatedResponse.getPage())))
                .andExpect(jsonPath("$.size", is(paginatedResponse.getSize())));

        verify(rideService, times(1)).getAllByDriverId(argThat(i -> i.compareTo(2L) == 0), any(), any(), any(), any());
    }

    @SneakyThrows
    @Test
    void getByIdWhenRideExists() {
        when(rideService.getById(1L))
                .thenReturn(ride);

        mockMvc.perform(get("/api/v1/rides/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(ride.getId().intValue())))
                .andExpect(jsonPath("$.passengerId", is(ride.getPassengerId().intValue())))
                .andExpect(jsonPath("$.driverId", is(ride.getDriverId().intValue())))
                .andExpect(jsonPath("$.destinationAddress", is(ride.getDestinationAddress())))
                .andExpect(jsonPath("$.departureAddress", is(ride.getDepartureAddress())))
                .andExpect(jsonPath("$.creationDate", is(ride.getCreationDate().toString())));

        verify(rideService, times(1)).getById(1L);
    }

    @SneakyThrows
    @Test
    void saveWhenRideNotExists() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        when(rideService.save(argThat(rating -> rating.getId().compareTo(1L) == 0))).thenReturn(ride);

        mockMvc.perform(post("/api/v1/rides")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(rideRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(ride.getId().intValue())))
                .andExpect(jsonPath("$.passengerId", is(ride.getPassengerId().intValue())))
                .andExpect(jsonPath("$.driverId", is(ride.getDriverId().intValue())))
                .andExpect(jsonPath("$.destinationAddress", is(ride.getDestinationAddress())))
                .andExpect(jsonPath("$.departureAddress", is(ride.getDepartureAddress())))
                .andExpect(jsonPath("$.creationDate", is(ride.getCreationDate().toString())));

        verify(rideService, times(1)).save(argThat(rating -> rating.getId().compareTo(1L) == 0));
    }

    @SneakyThrows
    @Test
    void updateWhenRideExists() {
        OffsetDateTime now = OffsetDateTime.now();

        RideRequest rideRequestToUpdate = rideRequest.toBuilder()
                .departureAddress("New Departure address 1")
                .destinationAddress("New Destination address 1")
                .creationDate(now)
                .build();

        Ride rideToUpdate = ride.toBuilder()
                .departureAddress("New Departure address 1")
                .destinationAddress("New Destination address 1")
                .creationDate(now)
                .build();

        RideResponse rideResponse1 = rideResponse.toBuilder()
                .departureAddress("New Departure address 1")
                .destinationAddress("New Destination address 1")
                .creationDate(now)
                .build();

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        when(rideService.update(argThat(rating -> rating.getId().compareTo(1L) == 0))).thenReturn(rideToUpdate);

        mockMvc.perform(put("/api/v1/rides")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(rideRequestToUpdate)))
                .andExpect(jsonPath("$.id", is(rideResponse1.getId().intValue())))
                .andExpect(jsonPath("$.passengerId", is(rideResponse1.getPassengerId().intValue())))
                .andExpect(jsonPath("$.driverId", is(rideResponse1.getDriverId().intValue())))
                .andExpect(jsonPath("$.destinationAddress", is(rideResponse1.getDestinationAddress())))
                .andExpect(jsonPath("$.departureAddress", is(rideResponse1.getDepartureAddress())));

    }

    @SneakyThrows
    @Test
    void changeRideStatusWhenRideExists() {
        Ride updatedRide = ride.toBuilder()
                .rideStatus(RideStatus.TO_PASSENGER)
                .build();

        when(rideService.changeStatus(argThat(arg -> arg.compareTo(1L) == 0), argThat(arg -> arg.compareTo(1L) == 0), any(RideStatus.class))).thenReturn(updatedRide);

        mockMvc.perform(patch("/api/v1/rides/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("userId", "1")
                        .param("status", "TO_PASSENGER"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(ride.getId().intValue())))
                .andExpect(jsonPath("$.passengerId", is(ride.getPassengerId().intValue())))
                .andExpect(jsonPath("$.driverId", is(ride.getDriverId().intValue())))
                .andExpect(jsonPath("$.destinationAddress", is(ride.getDestinationAddress())))
                .andExpect(jsonPath("$.departureAddress", is(ride.getDepartureAddress())))
                .andExpect(jsonPath("$.creationDate", is(ride.getCreationDate().toString())));

        verify(rideService, times(1)).changeStatus(argThat(arg -> arg.compareTo(1L) == 0), argThat(arg -> arg.compareTo(1L) == 0), any(RideStatus.class));
    }
}
