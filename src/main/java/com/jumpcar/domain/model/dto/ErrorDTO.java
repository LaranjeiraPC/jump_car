package com.jumpcar.domain.model.dto;

import io.micronaut.serde.annotation.Serdeable;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
@Serdeable
public class ErrorDTO implements Serializable {
    private String title;
    private String message;
}