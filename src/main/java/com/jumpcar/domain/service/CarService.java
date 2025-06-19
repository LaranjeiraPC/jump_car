package com.jumpcar.domain.service;

import com.jumpcar.domain.model.dto.CarDTO;

import java.util.List;

public interface CarService {

    CarDTO newCar(CarDTO carDTO);

    List<CarDTO> listAllCar();
}
