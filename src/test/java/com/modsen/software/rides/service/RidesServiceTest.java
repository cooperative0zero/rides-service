package com.modsen.software.rides.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.modsen.software.rides.client.PassengerServiceClient;
import com.modsen.software.rides.dto.PassengerResponse;
import com.modsen.software.rides.dto.RideRequest;
import com.modsen.software.rides.entity.Ride;
import com.modsen.software.rides.entity.enumeration.RideStatus;
import com.modsen.software.rides.kafka.event.BaseRideEvent;
import com.modsen.software.rides.repository.RideRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.kafka.core.KafkaTemplate;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import static com.modsen.software.rides.util.Constants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RidesServiceTest {

    @Mock
    private RideRepository rideRepository;

    @Mock
    private KafkaTemplate<String, BaseRideEvent> kafkaTemplate;

    @Mock
    private PassengerServiceClient passengerServiceClient;

    @InjectMocks
    private RideServiceImpl rideService;

    @Test
    void saveWhenRideNotExists() {
        when(passengerServiceClient.getById(1L)).thenReturn(passengerResponse);

        when(rideRepository.save(any(Ride.class))).thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));
        when(rideRepository.existsById(ride.getId())).thenReturn(false);

        Ride savedRide = rideService.save(ride);
        assertNotNull(savedRide);
        assertEquals(ride.getId(), savedRide.getId());
        assertEquals(ride.getDriverId(), savedRide.getDriverId());
        assertEquals(ride.getPassengerId(), savedRide.getPassengerId());
    }

    @Test
    void updateWhenRideExists() {
        OffsetDateTime now = OffsetDateTime.now();

        Ride rideToUpdate = ride.toBuilder()
                .departureAddress("New Departure address 1")
                .destinationAddress("New Destination address 1")
                .creationDate(now)
                .build();

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        when(rideRepository.save(any(Ride.class))).thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));
        when(rideRepository.existsById(ride.getId())).thenReturn(true);

        Ride updatedRide = rideService.update(rideToUpdate);

        assertNotNull(updatedRide);
        assertEquals(rideToUpdate.getId(), updatedRide.getId());
        assertEquals(rideToUpdate.getDriverId(), updatedRide.getDriverId());
        assertEquals(rideToUpdate.getPassengerId(), updatedRide.getPassengerId());
    }

    @Test
    void getByIdWhenRideExists() {
        when(rideRepository.findById(ride.getId())).thenReturn(Optional.of(ride));

        Ride result = rideService.getById(1L);
        assertEquals(ride.getId(), result.getId());
        assertEquals(ride.getDriverId(), result.getDriverId());
        assertEquals(ride.getPassengerId(), result.getPassengerId());
    }

    @Test
    void getAllWhenRidesExist() {
        Pageable pageable = PageRequest.of(0, 10, Sort.Direction.ASC, "id");
        List<Ride> toReturn = List.of(ride, anotherRide);

        when(rideRepository.findAll(pageable)).thenReturn(new PageImpl<>(toReturn));

        List<Ride> result = rideService.getAll(pageable.getPageNumber(), pageable.getPageSize(), "id", Sort.Direction.ASC.name());

        assertEquals(2L, result.size());
        assertEquals(ride.getId(), result.get(0).getId());
        assertEquals(anotherRide.getId(), result.get(1).getId());
    }

    @Test
    void getAllByPassengerIdWhenRidesExist() {
        Pageable pageable = PageRequest.of(0, 10, Sort.Direction.ASC, "id");
        List<Ride> toReturn = List.of(ride, anotherRide);

        when(rideRepository.findAllByPassengerId(ride.getPassengerId(), pageable))
                .thenReturn(new PageImpl<>(toReturn.stream()
                        .filter(item -> item.getPassengerId().compareTo(ride.getPassengerId()) == 0)
                        .toList()));

        List<Ride> result = rideService.getAllByPassengerId(ride.getPassengerId(), pageable.getPageNumber(), pageable.getPageSize(), "id", Sort.Direction.ASC.name());

        assertEquals(1, result.size());
        assertEquals(ride.getId(), result.get(0).getId());
    }

    @Test
    void getAllByDriverIdWhenRidesExist() {
        Pageable pageable = PageRequest.of(0, 10, Sort.Direction.ASC, "id");
        List<Ride> toReturn = List.of(ride, anotherRide);

        when(rideRepository.findAllByDriverId(anotherRide.getDriverId(), pageable))
                .thenReturn(new PageImpl<>(toReturn.stream()
                        .filter(item -> item.getDriverId().compareTo(anotherRide.getDriverId()) == 0)
                        .toList()));

        List<Ride> result = rideService.getAllByDriverId(anotherRide.getDriverId(), pageable.getPageNumber(), pageable.getPageSize(), "id", Sort.Direction.ASC.name());

        assertEquals(1, result.size());
        assertEquals(anotherRide.getId(), result.get(0).getId());
    }

    @Test
    void changeRideStatusWhenRideExists() {
        Ride updatedRide = ride.toBuilder()
                .rideStatus(RideStatus.TO_PASSENGER)
                .build();

        when(rideRepository.findById(argThat(arg -> arg.compareTo(1L) == 0))).thenReturn(Optional.of(updatedRide));
        when(rideRepository.save(any(Ride.class))).thenReturn(updatedRide);

        Ride result = rideService.changeStatus(ride.getId(), ride.getDriverId(), RideStatus.TO_PASSENGER);

        assertEquals(ride.getId(), result.getId());
        assertEquals(ride.getDriverId(), result.getDriverId());
        assertEquals(ride.getPassengerId(), result.getPassengerId());
        assertEquals(RideStatus.TO_PASSENGER, result.getRideStatus());
    }
}

