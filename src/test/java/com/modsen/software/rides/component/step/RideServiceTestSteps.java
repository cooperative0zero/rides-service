package com.modsen.software.rides.component.step;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.modsen.software.rides.client.PassengerServiceClient;
import com.modsen.software.rides.dto.PassengerResponse;
import com.modsen.software.rides.dto.RideRequest;
import com.modsen.software.rides.dto.RideResponse;
import com.modsen.software.rides.entity.Ride;
import com.modsen.software.rides.entity.enumeration.RideStatus;
import com.modsen.software.rides.integration.AbstractIntegrationTest;
import com.modsen.software.rides.repository.RideRepository;
import com.modsen.software.rides.util.Constants;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import jakarta.annotation.PostConstruct;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.OffsetDateTime;
import java.util.List;

import static com.modsen.software.rides.util.Constants.*;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@CucumberContextConfiguration
@SpringBootTest
@AutoConfigureMockMvc
public class RideServiceTestSteps extends AbstractIntegrationTest {

    @Autowired
    private RideRepository rideRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    private ResultActions result;

    @MockBean
    private PassengerServiceClient passengerServiceClient;

    @PostConstruct
    void setupServiceClientsMocks() {
        when(passengerServiceClient.getById(1L)).thenReturn(passengerResponse);
        when(passengerServiceClient.getById(2L)).thenReturn(anotherPassengerResponse);

        rideRepository.saveAll(List.of(ride, anotherRide));
    }

    @SneakyThrows
    @When("I request all rides from database through service")
    public void iRequestAllRidesFromDatabaseThroughService() {
        result = mockMvc.perform(get("/api/v1/rides")
                        .param("pageNumber", "0")
                        .param("pageSize", "10")
                        .param("sortBy", "id")
                        .param("sortOrder", "asc")
                        .contentType(MediaType.APPLICATION_JSON));
    }

    @SneakyThrows
    @Then("all rides request complete with code {int} \\(OK)")
    public void allRidesRequestCompleteWithCodeOK(int arg0) {
        result.andExpect(status().is(arg0));
    }

    @SneakyThrows
    @And("first rides page should be returned")
    public void firstRidesPageShouldBeReturned() {
        result.andExpect(jsonPath("$.items[0].id", is(ride.getId().intValue())))
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
    @When("I request all rides with passenger id = {int} from database through service")
    public void iRequestAllRidesWithPassengerIdFromDatabaseThroughService(int arg0) {
        result = mockMvc.perform(get("/api/v1/rides/passengers/{id}", arg0)
                        .param("pageNumber", "0")
                        .param("pageSize", "10")
                        .param("sortBy", "id")
                        .param("sortOrder", "asc")
                        .contentType(MediaType.APPLICATION_JSON));
    }

    @SneakyThrows
    @Then("all rides filtered by passenger id request complete with code {int} \\(OK)")
    public void allRidesFilteredByPassengerIdRequestCompleteWithCodeOK(int arg0) {
        result.andExpect(status().is(arg0));
    }

    @SneakyThrows
    @And("first page of filtered by passenger id = {int} rides should be returned")
    public void firstPageOfFilteredByPassengerIdRidesShouldBeReturned(int arg0) {
        result.andExpect(jsonPath("$.items[0].id", is(ride.getId().intValue())))
                .andExpect(jsonPath("$.items[0].passengerId", is(arg0)))
                .andExpect(jsonPath("$.items[0].driverId", is(ride.getDriverId().intValue())))
                .andExpect(jsonPath("$.total", is(1)))
                .andExpect(jsonPath("$.page", is(0)))
                .andExpect(jsonPath("$.size", is(10)));
    }

    @SneakyThrows
    @When("I request all rides with driver id = {int} from database through service")
    public void iRequestAllRidesWithDriverIdFromDatabaseThroughService(int arg0) {
        result =  mockMvc.perform(get("/api/v1/rides/drivers/{id}", arg0)
                .param("pageNumber", "0")
                .param("pageSize", "10")
                .param("sortBy", "id")
                .param("sortOrder", "asc")
                .contentType(MediaType.APPLICATION_JSON));
    }

    @SneakyThrows
    @Then("all rides filtered by driver id request complete with code {int} \\(OK)")
    public void allRidesFilteredByDriverIdRequestCompleteWithCodeOK(int arg0) {
        result.andExpect(status().is(arg0));
    }

    @SneakyThrows
    @And("first page of filtered by driver id = {int} rides should be returned")
    public void firstPageOfFilteredByDriverIdRidesShouldBeReturned(int arg0) {
        result.andExpect(jsonPath("$.items[0].id", is(anotherRide.getId().intValue())))
                .andExpect(jsonPath("$.items[0].passengerId", is(anotherRide.getPassengerId().intValue())))
                .andExpect(jsonPath("$.items[0].driverId", is(arg0)))
                .andExpect(jsonPath("$.total", is(1)))
                .andExpect(jsonPath("$.page", is(0)))
                .andExpect(jsonPath("$.size", is(10)));
    }

    @SneakyThrows
    @When("I request ride with id = {int} from database through service")
    public void iRequestRideWithIdFromDatabaseThroughService(int arg0) {
        result = mockMvc.perform(get("/api/v1/rides/{id}", arg0)
                .contentType(MediaType.APPLICATION_JSON));
    }

    @SneakyThrows
    @Then("find by id request complete with code {int} \\(OK) of rides")
    public void findByIdRequestCompleteWithCodeOKOfRides(int arg0) {
        result.andExpect(status().is(arg0));
    }

    @SneakyThrows
    @And("returned ride must be with status {string}")
    public void returnedRideMustBeWithStatus(String arg0) {
        result.andExpect(jsonPath("$.rideStatus", is(arg0)));
    }

    @SneakyThrows
    @Then("request complete with code {int}\\(NOT_FOUND) and indicates that specified ride not found")
    public void requestCompleteWithCodeNOT_FOUNDAndIndicatesThatSpecifiedRideNotFound(int arg0) {
        result.andExpect(status().is(arg0));
    }

    @SneakyThrows
    @When("I save new ride with passenger id = {int}, departureAddress = {string} and destinationAddress = {string}")
    public void iSaveNewRideWithPassengerIdDepartureAddressAndDestinationAddress(int arg0, String arg1, String arg2) {
        RideRequest rideRequest = Constants.rideRequest.toBuilder()
                        .id(null)
                        .passengerId((long) arg0)
                        .departureAddress(arg1)
                        .destinationAddress(arg2)
                        .build();

        result = mockMvc.perform(post("/api/v1/rides")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(rideRequest)));
    }

    @SneakyThrows
    @Then("saved ride with passenger id = {int}, departureAddress = {string} and destinationAddress = {string}")
    public void savedRideWithPassengerIdDepartureAddressAndDestinationAddress(int arg0, String arg1, String arg2) {
        result.andExpect(jsonPath("$.passengerId", is(arg0)))
                .andExpect(jsonPath("$.destinationAddress", is(arg2)))
                .andExpect(jsonPath("$.departureAddress", is(arg1)));
    }

    @SneakyThrows
    @When("I try to update ride with id = {int} changing departureAddress to {string}")
    public void iTryToUpdateRideWithIdChangingDepartureAddressTo(int arg0, String arg1) {
        OffsetDateTime now = OffsetDateTime.now();

        RideRequest rideRequestToUpdate = rideRequest.toBuilder()
                .id((long) arg0)
                .creationDate(now)
                .departureAddress(arg1)
                .destinationAddress("New Destination address 1")
                .build();

        result = mockMvc.perform(put("/api/v1/rides")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(rideRequestToUpdate)));
    }

    @SneakyThrows
    @Then("updated ride with departureAddress = {string} should be returned")
    public void updatedRideWithDepartureAddressShouldBeReturned(String arg0) {
        result.andExpect(jsonPath("$.departureAddress", is(arg0)));
    }

    @SneakyThrows
    @When("I try to change status of ride with id = {int} to {string}")
    public void iTryToChangeStatusOfRideWithIdTo(int arg0, String arg1) {
        result = mockMvc.perform(patch("/api/v1/rides/{id}", arg0)
                .contentType(MediaType.APPLICATION_JSON)
                .param("userId", "1")
                .param("status", arg1));
    }

    @SneakyThrows
    @Then("ride with changed status {string} should be returned")
    public void rideWithChangedStatusShouldBeReturned(String arg0) {
        result.andExpect(jsonPath("$.rideStatus", is(arg0)));
    }
}
