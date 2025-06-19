package com.jumpcar.domain.service;

import com.jumpcar.domain.model.dto.CarDTO;
import io.micronaut.data.model.Page;

import java.util.List;

public interface CarService {

    CarDTO newCar(CarDTO carDTO);

    List<CarDTO> listAllCar();

    CarDTO getCarByChassis(String chassis);

    void deleteCarByChassis(String chassis);

    CarDTO updateCarByChassis(String chassis, CarDTO carDTO);

    Page<CarDTO> listAllCarWithPagination(int page, int size);
}
