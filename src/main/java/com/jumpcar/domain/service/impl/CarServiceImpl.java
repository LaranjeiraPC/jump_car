package com.jumpcar.domain.service.impl;

import com.jumpcar.domain.exception.BusinessException;
import com.jumpcar.domain.exception.NotFoundException;
import com.jumpcar.domain.model.dto.CarDTO;
import com.jumpcar.domain.repository.CarRepository;
import com.jumpcar.domain.service.CarService;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import io.micronaut.data.model.Sort;
import io.micronaut.http.server.exceptions.InternalServerException;
import io.micronaut.transaction.annotation.Transactional;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Objects;

import static com.jumpcar.workflow.converter.CarConverter.INSTANCE;

@Slf4j
@Singleton
@Transactional
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class CarServiceImpl implements CarService {

    private final CarRepository carRepository;

    @Override
    public CarDTO newCar(CarDTO carDTO) {
        try {
            this.verifyObject(carDTO);

            log.info("Starting car record save flow");
            var car = INSTANCE.toCar(carDTO);
            log.info("Car <{}>", car);

            this.carRepository.save(car);

            log.info("Finish car record save flow");
            return carDTO;
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error creating new car: {}", e.getMessage(), e);
            throw new InternalServerException("Error creating new car", e);
        }
    }

    @Override
    public List<CarDTO> listAllCar() {
        log.info("Starting to list all cars");

        var cars = this.carRepository.findAll();
        log.info("Total cars found: {}", cars.size());

        log.info("Finish listing all cars");
        return INSTANCE.toCarDTO(cars);
    }

    @Override
    public CarDTO getCarByChassis(String chassis) {
        try {
            this.verifyChassis(chassis);

            log.info("Starting to search car by chassis: {}", chassis);
            var car = this.carRepository.findByChassisContainsIgnoreCase(chassis)
                    .orElseThrow(() -> new NotFoundException("Car not found for chassis: " + chassis));
            log.info("Car found: {}", car);
            return INSTANCE.toCarDTO(car);
        } catch (BusinessException | NotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error searching car by chassis: {}", e.getMessage(), e);
            throw new InternalServerException("Error searching car by chassis", e);
        }
    }

    @Override
    public void deleteCarByChassis(String chassis) {
        try {
            this.verifyChassis(chassis);
            log.info("Starting to delete car by chassis: {}", chassis);
            this.carRepository.deleteByChassisContainsIgnoreCase(chassis);
            log.info("Car deleted successfully for chassis: {}", chassis);
        } catch (Exception e) {
            log.error("Error deleting car by chassis: {}", e.getMessage(), e);
            throw new InternalServerException("Error deleting car by chassis", e);
        }
    }

    @Override
    public CarDTO updateCarByChassis(String chassis, CarDTO carDTO) {
        try {
            this.verifyChassis(chassis);
            this.verifyObject(carDTO);

            log.info("Starting to update car by chassis: {}", chassis);
            var existingCar = this.carRepository.findByChassisContainsIgnoreCase(chassis)
                    .orElseThrow(() -> new NotFoundException("Car not found for chassis: " + chassis));

            var updatedCar = INSTANCE.toCar(carDTO);
            updatedCar.setId(existingCar.getId());

            this.carRepository.update(updatedCar);
            log.info("Car updated successfully for chassis: {}", chassis);
            return INSTANCE.toCarDTO(updatedCar);
        } catch (BusinessException | NotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error updating car by chassis: {}", e.getMessage(), e);
            throw new InternalServerException("Error updating car by chassis", e);
        }
    }

    public Page<CarDTO> listAllCarWithPagination(int page, int size) {
        try {
            log.info("Starting to list all cars with pagination: page= <{}>, size= <{}>", page, size);

            Pageable pageable = Pageable.from(page, size, Sort.of(Sort.Order.asc("id")));
            Page<CarDTO> carPage = this.carRepository.findAll(pageable)
                    .map(INSTANCE::toCarDTO);

            log.info("Total cars pagination found: {}", carPage.getTotalSize());
            return carPage;
        } catch (Exception e) {
            log.error("Error listing cars with pagination: {}", e.getMessage(), e);
            throw new InternalServerException("Error listing cars with pagination", e);
        }
    }

    private void verifyObject(CarDTO carDTO) {
        if (Objects.isNull(carDTO)) {
            log.error("CarDTO is null");
            throw new BusinessException("CarDTO cannot be null");
        }
    }

    private void verifyChassis(String chassis) {
        if (Objects.isNull(chassis) || chassis.isBlank()) {
            log.error("Chassis is null or blank");
            throw new BusinessException("Chassis cannot be null or blank");
        }
    }

}
