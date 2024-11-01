package com.modsen.software.rides.repository;

import com.modsen.software.rides.entity.Ride;
import com.modsen.software.rides.entity.enumeration.RideStatus;
import com.modsen.software.rides.util.Constants;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import static com.modsen.software.rides.util.Constants.*;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
public class RidesRepositoryTest {

    @Autowired
    private RideRepository repository;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setup() {
        jdbcTemplate.execute("TRUNCATE TABLE rides RESTART IDENTITY;");
    }

    @Test
    void saveWhenRideNotExists() {
        Ride savedRide = repository.save(ride);

        assertNotNull(savedRide);
        assertEquals(ride.getId(), savedRide.getId());
        assertEquals(ride.getPassengerId(), savedRide.getPassengerId());
        assertEquals(ride.getDriverId(), savedRide.getDriverId());
    }

    @Test
    void updateWhenRideExists() {
        repository.save(ride);

        Ride ride = Constants.ride.toBuilder()
                .departureAddress("Update Departure address")
                .passengerId(anotherPassengerResponse.getId())
                .build();

        Ride updatedRide = repository.save(ride);
        assertEquals(ride.getId(), updatedRide.getId());
        assertEquals(ride.getPassengerId(), updatedRide.getPassengerId());
        assertEquals(ride.getDriverId(), updatedRide.getDriverId());
        assertEquals(ride.getDepartureAddress(), updatedRide.getDepartureAddress());
    }

    @Test
    void changeRideStatusWhenRideExists() {
        repository.save(ride);

        int linesChanged = repository.changeStatus(ride.getId(), RideStatus.TO_PASSENGER);
        assertEquals(1, linesChanged);

        entityManager.clear();

        Optional<Ride> updated = repository.findById(ride.getId());

        assertTrue(updated.isPresent());
        assertEquals(RideStatus.TO_PASSENGER, updated.get().getRideStatus());
        assertEquals(ride.getPassengerId(), updated.get().getPassengerId());
        assertEquals(ride.getDriverId(), updated.get().getDriverId());
    }

    @Test
    void findByIdWhenRideExists() {
        Ride savedRide = repository.save(ride);

        Optional<Ride> foundRide = repository.findById(savedRide.getId());
        assertTrue(foundRide.isPresent());
        assertEquals(savedRide.getDepartureAddress(), foundRide.get().getDepartureAddress());
        assertEquals(savedRide.getPassengerId(), foundRide.get().getPassengerId());
        assertEquals(savedRide.getDriverId(), foundRide.get().getDriverId());
    }

    @Test
    void findAllWhenRidesExist() {
        repository.save(ride);
        repository.save(anotherRide);

        List<Ride> ratings = repository.findAll();
        assertEquals(2, ratings.size());
        assertEquals(ride.getId(), ratings.get(0).getId());
        assertEquals(anotherRide.getId(), ratings.get(1).getId());
    }

    @Test
    void findAllByPassengerIdWhenRidesExist() {
        Pageable pageable = PageRequest.of(
                0,
                10,
                Sort.Direction.ASC,
                "id"
        );

        repository.save(ride);
        repository.save(anotherRide);

        List<Ride> ratings = repository.findAllByPassengerId(ride.getPassengerId(), pageable).getContent();

        assertEquals(1, ratings.size());
        assertEquals(ride.getId(), ratings.get(0).getId());
    }

    @Test
    void findAllByDriverIdWhenRidesExist() {
        Pageable pageable = PageRequest.of(
                0,
                10,
                Sort.Direction.ASC,
                "id"
        );

        repository.save(ride);
        repository.save(anotherRide);

        List<Ride> ratings = repository.findAllByDriverId(anotherRide.getDriverId(), pageable).getContent();

        assertEquals(1, ratings.size());
        assertEquals(anotherRide.getId(), ratings.get(0).getId());
    }
}
