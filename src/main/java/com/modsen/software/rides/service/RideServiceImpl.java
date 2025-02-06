package com.modsen.software.rides.service;

import com.modsen.software.rides.client.PassengerServiceClient;
import com.modsen.software.rides.entity.Ride;
import com.modsen.software.rides.entity.enumeration.RideStatus;
import com.modsen.software.rides.entity.enumeration.UserType;
import com.modsen.software.rides.exception.RideAlreadyExistsException;
import com.modsen.software.rides.exception.RideDowngradingStatus;
import com.modsen.software.rides.exception.RideNotExistsException;
import com.modsen.software.rides.exception.RideStatusChangeNotPermitted;
import com.modsen.software.rides.kafka.event.BaseRideEvent;
import com.modsen.software.rides.kafka.event.SelectionDriverEvent;
import com.modsen.software.rides.kafka.event.StatusChangedEvent;
import com.modsen.software.rides.redis.aspect.CacheAction;
import com.modsen.software.rides.redis.aspect.CacheableRide;
import com.modsen.software.rides.redis.service.RedisService;
import com.modsen.software.rides.repository.RideRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RideServiceImpl {

    private final RideRepository rideRepository;

    private final PassengerServiceClient passengerServiceClient;

    private final KafkaTemplate<String, BaseRideEvent> kafkaTemplate;

    private final RedisService redisService;

    @Transactional(readOnly = true)
//    @CacheableRide(action = CacheAction.GET)
    @Cacheable(value = "rides", key = "#id")
    public Ride getById(Long id) {
//        Ride value = (Ride) redisService.getValue(id.toString());
//
//        if (value == null) {
//            value = rideRepository.findById(id)
//                    .orElseThrow(()-> new RideNotExistsException(String.format("Ride with id = %d not exists", id)));
//            redisService.putValue(id.toString(), value);
//        }

        var value = rideRepository.findById(id)
                .orElseThrow(()-> new RideNotExistsException(String.format("Ride with id = %d not exists", id)));

        return value;
    }

    @Transactional(readOnly = true)
    public List<Ride> getAll(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        return rideRepository.findAll(PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.fromString(sortOrder), sortBy)))
                .getContent();
    }

    @Transactional(readOnly = true)
    public List<Ride> getAllByPassengerId(Long id, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        return rideRepository.findAllByPassengerId(id, PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.fromString(sortOrder), sortBy)))
                .getContent();
    }

    @Transactional(readOnly = true)
    public List<Ride> getAllByDriverId(Long id, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        return rideRepository.findAllByDriverId(id, PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.fromString(sortOrder), sortBy)))
                .getContent();
    }

    @Transactional
    @CachePut(value = "rides", key = "#request.id")
//    @CacheableRide(action = CacheAction.PUT)
    public Ride save(Ride request) {

        if (request.getId() != null && rideRepository.existsById(request.getId())) {
            throw new RideAlreadyExistsException(String.format("Ride with id = %d already exists", request.getId()));
        }

        passengerServiceClient.getById(request.getPassengerId());
        request.setCreationDate(OffsetDateTime.now());
        request.setPrice(calculateEstimatedPriceForPassenger());

        var savedRide = rideRepository.save(request);

//        redisService.putValue(savedRide.getId().toString(), savedRide);

        kafkaTemplate.send("ride_events", new SelectionDriverEvent(savedRide.getId()));
        kafkaTemplate.send("ride_events", new StatusChangedEvent(savedRide.getId(), request.getRideStatus().toString(),
                RideStatus.CREATED.toString(), UserType.PASSENGER.toString()));

        return savedRide;
    }

    @Transactional
    @CachePut(value = "rides", key = "#request.id")
//    @CacheableRide(action = CacheAction.PUT)
    public Ride update(Ride request) {
        if (!rideRepository.existsById(request.getId()))
            throw new RideNotExistsException(String.format("Ride with id = %d not exists", request.getId()));

//        redisService.putValue(request.getId().toString(), request);

        return rideRepository.save(request);
    }

    @Transactional
    @CachePut(value = "rides", key = "#id")
//    @CacheableRide(action = CacheAction.PUT)
    public Ride changeStatus(Long id, Long userId, RideStatus newStatus) {
        var userType = getSessionUserType();

        if (!verifyStatusByType(newStatus, userType))
            throw new RideStatusChangeNotPermitted("Status change not permitted for userType = " + userType);

        var ride = rideRepository.findById(id).orElseThrow(() ->
                new RideNotExistsException(String.format("Ride with id = %d not exists", id)));

        if (ride.getRideStatus().compareTo(newStatus) > 0)
            throw new RideDowngradingStatus(String.format("Can't downgrade status for the ride with id = %d", id));

        if (!(userId.compareTo(ride.getPassengerId()) == 0 || userId.compareTo(ride.getDriverId()) == 0))
            throw new RideStatusChangeNotPermitted("User that are changing status are not in ride");

        ride.setRideStatus(newStatus);

//        redisService.putValue(ride.getId().toString(), ride);

        kafkaTemplate.send("ride_events", new StatusChangedEvent(id, ride.getRideStatus().toString(),
                newStatus.toString(), userType.toString()));

        return rideRepository.save(ride);
    }

    private boolean verifyStatusByType(RideStatus newStatus, UserType userType) {
        return switch (userType) {
            case DRIVER -> newStatus != RideStatus.CREATED;
            case PASSENGER -> newStatus == RideStatus.CREATED || newStatus == RideStatus.CANCELLED;
            case ADMIN -> true;
        };
    }

    private BigDecimal calculateEstimatedPriceForPassenger() {
        return new BigDecimal(123);
    }

    private Long getSessionUserId() {
        return 1L;
    }

    private UserType getSessionUserType() {
        return UserType.DRIVER;
    }
}