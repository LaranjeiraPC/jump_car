package com.jumpcar.controller;

import com.jumpcar.domain.model.dto.CarDTO;
import com.jumpcar.domain.service.CarService;
import io.micronaut.data.model.Page;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Tag(name = "Car")
@Controller(value = "car")
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class CarController {

    private final CarService carService;

    @Post(produces = "application/json")
    @Operation(summary = "New Car", description = "New Car")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Car created successfully"),
            @ApiResponse(responseCode = "500", description = "Car - Internal Server Error")
    })
    HttpResponse<CarDTO> newCar(@RequestBody @Body CarDTO carDTO) {
        return HttpResponse.created(this.carService.newCar(carDTO));
    }

    @Get(produces = "application/json")
    @Operation(summary = "Get all Car", description = "Get all Car - no pagination")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Car search successfully"),
            @ApiResponse(responseCode = "500", description = "Car - Internal Server Error")
    })
    HttpResponse<List<CarDTO>> getAllCars() {
        return HttpResponse.ok(this.carService.listAllCar());
    }

    @Get(value = "/pagination", produces = "application/json")
    @Operation(summary = "Get all Car", description = "Get all Car - with pagination")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Car search successfully"),
            @ApiResponse(responseCode = "500", description = "Car - Internal Server Error")
    })
    HttpResponse<Page<CarDTO>> getAllCarsWithPagination(
            @QueryValue(defaultValue = "0") int page,
            @QueryValue(defaultValue = "10") int size) {
        return HttpResponse.ok(this.carService.listAllCarWithPagination(page, size));
    }

    @Get(value = "/chassis/{chassis}", produces = "application/json")
    @Operation(summary = "Get Car by Chassis", description = "Get Car by Chassis")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Car search successfully"),
            @ApiResponse(responseCode = "404", description = "Car not found"),
            @ApiResponse(responseCode = "500", description = "Car - Internal Server Error")
    })
    HttpResponse<CarDTO> getCarByChassis(String chassis) {
        return HttpResponse.ok(this.carService.getCarByChassis(chassis));
    }

    @Delete(value = "/chassis/{chassis}", produces = "application/json")
    @Operation(summary = "Delete Car by Chassis", description = "Delete Car by Chassis")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Car deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Car not found"),
            @ApiResponse(responseCode = "500", description = "Car - Internal Server Error")
    })
    HttpResponse<Void> deleteCarByChassis(String chassis) {
        this.carService.deleteCarByChassis(chassis);
        return HttpResponse.noContent();
    }

    @Patch(value = "/chassis/{chassis}", produces = "application/json")
    @Operation(summary = "Update Car by Chassis", description = "Update Car by Chassis")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Car updated successfully"),
            @ApiResponse(responseCode = "404", description = "Car not found"),
            @ApiResponse(responseCode = "500", description = "Car - Internal Server Error")
    })
    HttpResponse<CarDTO> updateCarByChassis(String chassis, @RequestBody @Body CarDTO carDTO) {
        return HttpResponse.ok(this.carService.updateCarByChassis(chassis, carDTO));
    }
}
