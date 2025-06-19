package com.jumpcar.domain.model.dto;

import io.micronaut.core.annotation.Nullable;
import io.micronaut.serde.annotation.Serdeable;
import lombok.Builder;
import lombok.Data;

@Data
@Serdeable
@Builder
public class CarDTO {

    @Nullable
    private Long id;

    @Nullable
    private String model;

    @Nullable
    private String manufacturer;

    @Nullable
    private String color;

    @Nullable
    private String engine;

    @Nullable
    private String chassis;

}
