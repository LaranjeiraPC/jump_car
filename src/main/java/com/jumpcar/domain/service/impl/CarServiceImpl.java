package com.jumpcar.domain.service.impl;

import com.jumpcar.domain.exception.BusinessException;
import com.jumpcar.domain.model.dto.CarDTO;
import com.jumpcar.domain.repository.CarRepository;
import com.jumpcar.domain.service.CarService;
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

    private void verifyObject(CarDTO carDTO) {
        if (Objects.isNull(carDTO)) {
            log.error("CarDTO is null");
            throw new BusinessException("CarDTO cannot be null");
        } else if (Objects.isNull(carDTO.getChassis())) {
            log.error("Chassis is null");
            throw new BusinessException("CarDTO cannot be null");
        }
    }

}
