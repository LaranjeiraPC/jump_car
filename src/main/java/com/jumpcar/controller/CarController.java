package com.jumpcar.controller;

import com.jumpcar.domain.model.dto.CarDTO;
import com.jumpcar.domain.service.CarService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
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
    @Operation(summary = "Get all Car", description = "Get all Car")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Car search successfully"),
            @ApiResponse(responseCode = "500", description = "Car - Internal Server Error")
    })
    HttpResponse<List<CarDTO>> getAllCars() {
        return HttpResponse.ok(this.carService.listAllCar());
    }
}
