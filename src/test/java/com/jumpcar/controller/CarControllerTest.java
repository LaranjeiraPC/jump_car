package com.jumpcar.controller;

import com.jumpcar.domain.exception.BusinessException;
import com.jumpcar.domain.exception.NotFoundException;
import com.jumpcar.domain.model.dto.CarDTO;
import com.jumpcar.domain.service.CarService;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import io.micronaut.http.HttpStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CarControllerTest {

    @Mock
    private CarService carService;

    @InjectMocks
    private CarController carController;

    @Test
    void newCarShouldReturnCreatedCarWithStatus201() {
        var carDTO = CarDTO.builder()
                .id(1L)
                .model("TOYOTA")
                .manufacturer("Corolla")
                .build();

        when(carService.newCar(carDTO)).thenReturn(carDTO);

        var response = carController.newCar(carDTO);

        assertEquals(HttpStatus.CREATED, response.status());
        assertEquals(carDTO, response.body());
    }

    @Test
    void getAllCarsShouldReturnCarListWithStatus200() {
        var cars = List.of(
                CarDTO.builder()
                        .id(1L)
                        .model("TOYOTA")
                        .manufacturer("Corolla")
                        .build(),
                CarDTO.builder()
                        .id(2L)
                        .model("HONDA")
                        .manufacturer("Civic")
                        .build()
        );

        when(carService.listAllCar()).thenReturn(cars);

        var response = carController.getAllCars();

        assertEquals(HttpStatus.OK, response.status());
        assertEquals(cars, response.body());
    }

    @Test
    void getAllCarsWithPaginationShouldReturnPagedCarsWithStatus200() {
        int page = 0;
        int size = 10;
        var cars = List.of(CarDTO.builder()
                .id(1L)
                .model("TOYOTA")
                .manufacturer("Corolla")
                .build());
        var pagedCars = Page.of(cars, Pageable.from(page, size), 1);

        when(carService.listAllCarWithPagination(page, size)).thenReturn(pagedCars);

        var response = carController.getAllCarsWithPagination(page, size);

        assertEquals(HttpStatus.OK, response.status());
        assertEquals(pagedCars, response.body());
    }

    @Test
    void getCarByChassisShouldReturnCarWithStatus200() {
        var chassis = "ABC123";
        var carDTO = CarDTO.builder()
                .id(1L)
                .model("TOYOTA")
                .manufacturer("Corolla")
                .build();

        when(carService.getCarByChassis(chassis)).thenReturn(carDTO);

        var response = carController.getCarByChassis(chassis);

        assertEquals(HttpStatus.OK, response.status());
        assertEquals(carDTO, response.body());
    }

    @Test
    void deleteCarByChassisShouldReturnStatus204() {
        var chassis = "ABC123";
        doNothing().when(carService).deleteCarByChassis(chassis);

        var response = carController.deleteCarByChassis(chassis);

        assertEquals(HttpStatus.NO_CONTENT, response.status());
        verify(carService).deleteCarByChassis(chassis);
    }

    @Test
    void updateCarByChassisShouldReturnUpdatedCarWithStatus200() {
        var chassis = "ABC123";
        var carDTO = CarDTO.builder()
                .id(1L)
                .model("TOYOTA")
                .manufacturer("Corolla")
                .build();

        when(carService.updateCarByChassis(chassis, carDTO)).thenReturn(carDTO);

        var response = carController.updateCarByChassis(chassis, carDTO);

        assertEquals(HttpStatus.OK, response.status());
        assertEquals(carDTO, response.body());
    }

    @Test
    void newCarShouldPropagateBusinessException() {
        var carDTO = CarDTO.builder()
                .id(1L)
                .model("TOYOTA")
                .manufacturer("Corolla")
                .build();

        when(carService.newCar(carDTO)).thenThrow(new BusinessException("Invalid car"));

        assertThrows(BusinessException.class, () -> carController.newCar(carDTO));
    }

    @Test
    void getCarByChassisShouldPropagateNotFoundException() {
        var chassis = "ABC123";

        when(carService.getCarByChassis(chassis)).thenThrow(new NotFoundException("Car not found"));

        assertThrows(NotFoundException.class, () -> carController.getCarByChassis(chassis));
    }
}