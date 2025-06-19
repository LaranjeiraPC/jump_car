package com.jumpcar.workflow.converter;

import com.jumpcar.domain.model.dto.CarDTO;
import com.jumpcar.domain.model.entity.Car;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDateTime;
import java.util.List;

import static org.mapstruct.factory.Mappers.getMapper;

@Mapper(imports = LocalDateTime.class)
public interface CarConverter {

    CarConverter INSTANCE = getMapper(CarConverter.class);

    CarDTO toCarDTO(Car car);

    @Mapping(target = "id", ignore = true)
    Car toCar(CarDTO car);

    List<CarDTO> toCarDTO(List<Car> car);
}
