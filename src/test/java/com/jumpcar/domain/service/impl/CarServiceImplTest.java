package com.jumpcar.domain.service.impl;

import com.jumpcar.domain.exception.BusinessException;
import com.jumpcar.domain.exception.NotFoundException;
import com.jumpcar.domain.model.dto.CarDTO;
import com.jumpcar.domain.model.entity.Car;
import com.jumpcar.domain.repository.CarRepository;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import io.micronaut.data.model.Sort;
import io.micronaut.http.server.exceptions.InternalServerException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CarServiceImplTest {

    @Mock
    private CarRepository carRepository;

    @InjectMocks
    private CarServiceImpl carService;

    @Test
    void newCarShouldSaveCarSuccessfully() {
        var carDTO = CarDTO.builder()
                .id(1L)
                .model("TOYOTA")
                .manufacturer("Corolla")
                .build();

        var car = Car.builder()
                .id(1L)
                .model("TOYOTA")
                .manufacturer("Corolla")
                .build();

        when(this.carRepository.save(any(Car.class))).thenReturn(car);

        var result = this.carService.newCar(carDTO);

        assertNotNull(result);
        assertEquals(carDTO.getChassis(), result.getChassis());
        verify(this.carRepository).save(any(Car.class));
    }

    @Test
    void newCarShouldThrowBusinessExceptionWhenCarDTOIsNull() {
        assertThrows(BusinessException.class, () -> this.carService.newCar(null));
    }

    @Test
    void newCarShouldThrowException() {
        var carDTO = CarDTO.builder()
                .id(1L)
                .model("TOYOTA")
                .manufacturer("Corolla")
                .build();

        when(this.carRepository.save(any(Car.class))).thenThrow(new RuntimeException("Database error"));
        assertThrows(InternalServerException.class, () -> this.carService.newCar(carDTO));
    }

    @Test
    void getCarByChassisShouldReturnCarWhenFound() {
        String chassis = "ABC123";
        var car = Car.builder()
                .id(1L)
                .chassis("ABC123")
                .model("TOYOTA")
                .manufacturer("Corolla")
                .build();

        when(this.carRepository.findByChassisContainsIgnoreCase(chassis)).thenReturn(Optional.of(car));

        CarDTO result = this.carService.getCarByChassis(chassis);

        assertNotNull(result);
        assertEquals(chassis, result.getChassis());
        verify(this.carRepository).findByChassisContainsIgnoreCase(chassis);
    }

    @Test
    void getCarByChassisShouldThrowNotFoundExceptionWhenCarNotFound() {
        String chassis = "XYZ789";

        when(this.carRepository.findByChassisContainsIgnoreCase(chassis)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> this.carService.getCarByChassis(chassis));
    }

    @Test
    void getCarByChassisShouldThrowInternalServerException() {
        String chassis = "XYZ789";

        when(this.carRepository.findByChassisContainsIgnoreCase(anyString())).thenThrow(new RuntimeException("Database error"));

        assertThrows(InternalServerException.class, () -> this.carService.getCarByChassis(chassis));
    }

    @Test
    void getCarByChassisShouldThrowBusinessExceptionWhenChassisIsBlank() {
        assertThrows(BusinessException.class, () -> this.carService.getCarByChassis(""));
    }

    @Test
    void getCarByChassisShouldThrowBusinessExceptionWhenChassisIsNull() {
        assertThrows(BusinessException.class, () -> this.carService.getCarByChassis(null));
    }

    @Test
    void deleteCarByChassisShouldDeleteCarSuccessfully() {
        String chassis = "ABC123";

        doNothing().when(this.carRepository).deleteByChassisContainsIgnoreCase(chassis);

        this.carService.deleteCarByChassis(chassis);

        verify(this.carRepository).deleteByChassisContainsIgnoreCase(chassis);
    }

    @Test
    void deleteCarByChassisShouldThrowExceptionWhenErrorOccurs() {
        String chassis = "ABC123";

        doThrow(new RuntimeException("Database error")).when(this.carRepository).deleteByChassisContainsIgnoreCase(chassis);

        assertThrows(InternalServerException.class, () -> this.carService.deleteCarByChassis(chassis));
    }

    @Test
    void listAllCarWithPaginationShouldReturnPagedCars() {
        int page = 0;
        int size = 10;
        Pageable pageable = Pageable.from(page, size, Sort.of(Sort.Order.asc("id")));
        Page<Car> carPage = Page.of(List.of(Car.builder()
                .id(1L)
                .model("TOYOTA")
                .manufacturer("Corolla")
                .build()), pageable, 1);

        when(this.carRepository.findAll(pageable)).thenReturn(carPage);

        Page<CarDTO> result = this.carService.listAllCarWithPagination(page, size);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        verify(this.carRepository).findAll(pageable);
    }

    @Test
    void updateCarByChassisShouldUpdateCarSuccessfully() {
        String chassis = "ABC123";
        var carDTO = CarDTO.builder()
                .id(1L)
                .chassis("ABC123")
                .model("TOYOTA")
                .manufacturer("Corolla")
                .build();
        var existingCar = Car.builder()
                .id(1L)
                .chassis("ABC123")
                .model("TOYOTA")
                .manufacturer("Corolla")
                .build();
        var updatedCar = Car.builder()
                .id(1L)
                .chassis("ABC123")
                .model("TOYOTA")
                .manufacturer("Corolla")
                .build();

        when(this.carRepository.findByChassisContainsIgnoreCase(chassis)).thenReturn(Optional.of(existingCar));
        when(this.carRepository.update(any(Car.class))).thenReturn(updatedCar);

        CarDTO result = this.carService.updateCarByChassis(chassis, carDTO);

        assertNotNull(result);
        assertEquals(chassis, result.getChassis());
        verify(this.carRepository).findByChassisContainsIgnoreCase(chassis);
        verify(this.carRepository).update(any(Car.class));
    }

    @Test
    void updateCarByChassisShouldThrowNotFoundExceptionWhenCarNotFound() {
        String chassis = "XYZ789";
        var carDTO = CarDTO.builder()
                .id(1L)
                .model("TOYOTA")
                .manufacturer("Corolla")
                .build();

        when(this.carRepository.findByChassisContainsIgnoreCase(chassis)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> this.carService.updateCarByChassis(chassis, carDTO));
    }

    @Test
    void updateCarByChassisShouldThrowInternalServerException() {
        String chassis = "XYZ789";
        var carDTO = CarDTO.builder()
                .id(1L)
                .model("TOYOTA")
                .manufacturer("Corolla")
                .build();

        when(this.carRepository.findByChassisContainsIgnoreCase(chassis)).thenThrow(new RuntimeException("Database error"));

        assertThrows(InternalServerException.class, () -> this.carService.updateCarByChassis(chassis, carDTO));
    }



    @Test
    void listAllCarWithPaginationShouldThrowInternalServerException() {
        int page = 0;
        int size = 10;
        Pageable pageable = Pageable.from(page, size, Sort.of(Sort.Order.asc("id")));

        when(this.carRepository.findAll(pageable)).thenThrow(new RuntimeException("Database error"));

        assertThrows(InternalServerException.class, () -> this.carService.listAllCarWithPagination(page, size));
    }

    @Test
    void listAllCarShouldReturnAllCarsSuccessfully() {
        var cars = List.of(
                Car.builder().id(1L).model("TOYOTA").manufacturer("Corolla").build(),
                Car.builder().id(2L).model("HONDA").manufacturer("Civic").build()
        );

        when(this.carRepository.findAll()).thenReturn(cars);

        var result = this.carService.listAllCar();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(this.carRepository).findAll();
    }

    @Test
    void listAllCarShouldReturnEmptyListWhenNoCarsFound() {
        when(this.carRepository.findAll()).thenReturn(List.of());

        var result = this.carService.listAllCar();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(this.carRepository).findAll();
    }

}
