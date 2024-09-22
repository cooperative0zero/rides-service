package com.modsen.software.rides.service.impl;

import com.modsen.software.rides.dto.RideRequest;
import com.modsen.software.rides.dto.RideResponse;
import com.modsen.software.rides.entity.Ride;
import com.modsen.software.rides.entity.enumeration.RideStatus;
import com.modsen.software.rides.exception.RideAlreadyExistsException;
import com.modsen.software.rides.exception.RideNotExistsException;
import com.modsen.software.rides.repository.RideRepository;
import com.modsen.software.rides.service.RideService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class RideServiceImpl implements RideService {

    private final RideRepository rideRepository;

    private final ConversionService conversionService;

    @Override
    @Transactional(readOnly = true)
    public RideResponse getById(Long id) {
        var ride = rideRepository.findById(id)
                .orElseThrow(()-> new RideNotExistsException(String.format("Ride with id = %d not exists", id)));
        return conversionService.convert(ride, RideResponse.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RideResponse> getAll(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        return rideRepository.findAll(PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.fromString(sortOrder), sortBy)))
                .getContent()
                .stream()
                .map((o)-> conversionService.convert(o, RideResponse.class))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<RideResponse> getAllByPassengerId(Long id, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        return rideRepository.findAllByPassengerId(id, PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.fromString(sortOrder), sortBy)))
                .getContent()
                .stream()
                .map((o)-> conversionService.convert(o, RideResponse.class))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<RideResponse> getAllByDriverId(Long id, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        return rideRepository.findAllByDriverId(id, PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.fromString(sortOrder), sortBy)))
                .getContent()
                .stream()
                .map((o)-> conversionService.convert(o, RideResponse.class))
                .toList();
    }

    @Override
    @Transactional
    public RideResponse save(RideRequest request) {
        var ride = Objects.requireNonNull(conversionService.convert(request, Ride.class));

        if (ride.getId() != null && rideRepository.existsById(ride.getId())) {
            throw new RideAlreadyExistsException(String.format("Ride with id = %d already exists", ride.getId()));
        } else {
            rideRepository.save(ride);
        }

        return conversionService.convert(ride, RideResponse.class);
    }

    @Override
    @Transactional
    public RideResponse update(RideRequest request) {
        if (!rideRepository.existsById(request.getId()))
            throw new RideNotExistsException(String.format("Ride with id = %d not exists", request.getId()));

        var updated = Objects.requireNonNull(conversionService.convert(request, Ride.class));

        return conversionService.convert(rideRepository.save(updated), RideResponse.class);
    }

    @Override
    @Transactional
    public RideResponse changeStatus(Long id, RideStatus newStatus) {
        if (rideRepository.changeStatus(id, newStatus.toString()) == 0)
            throw new RideNotExistsException(String.format("Ride with id = %d not exists", id));

        return conversionService.convert(rideRepository.findById(id).get(), RideResponse.class);
    }
}