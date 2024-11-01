package com.modsen.software.rides.contract;

import com.modsen.software.rides.client.PassengerServiceClient;
import com.modsen.software.rides.dto.PassengerResponse;
import com.modsen.software.rides.entity.Ride;
import com.modsen.software.rides.entity.enumeration.RideStatus;
import com.modsen.software.rides.integration.AbstractIntegrationTest;
import com.modsen.software.rides.repository.RideRepository;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.contract.verifier.messaging.boot.AutoConfigureMessageVerifier;
import org.springframework.test.web.servlet.MockMvc;

import java.time.OffsetDateTime;
import java.util.List;

import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMessageVerifier
@AutoConfigureMockMvc
public class RidesBaseContractTest extends AbstractIntegrationTest {
    private static Ride ride;
    private static Ride secondRide;

    @Autowired
    private RideRepository rideRepository;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PassengerServiceClient passengerServiceClient;

    static {
        OffsetDateTime now = OffsetDateTime.now();

        ride = Ride.builder()
                .id(1L)
                .passengerId(1L)
                .driverId(1L)
                .departureAddress("Departure address 1")
                .destinationAddress("Destination address 1")
                .creationDate(now)
                .rideStatus(RideStatus.CREATED)
                .build();

        secondRide = Ride.builder()
                .id(2L)
                .passengerId(2L)
                .driverId(2L)
                .departureAddress("Departure address 2")
                .destinationAddress("Destination address 2")
                .creationDate(now)
                .rideStatus(RideStatus.CREATED)
                .build();
    }

    @PostConstruct
    void setupServiceClientsMocks() {

        when(passengerServiceClient.getById(1L)).thenReturn(new PassengerResponse(
                1L,
                "First Middle Last",
                "example@mail.com",
                "987654321",
                false
        ));

        when(passengerServiceClient.getById(2L)).thenReturn(new PassengerResponse(
                2L,
                "First2 Middle2 Last2",
                "example2@mail.com",
                "123456789",
                false
        ));

        this.rideRepository.saveAll(List.of(ride, secondRide));

        RestAssuredMockMvc.mockMvc(mockMvc);
    }
}
