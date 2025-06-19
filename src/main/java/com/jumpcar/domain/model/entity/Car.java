package com.jumpcar.domain.model.entity;

import io.micronaut.data.annotation.DateCreated;
import io.micronaut.data.annotation.DateUpdated;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "car", schema = "")
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String model;
    private String manufacturer;
    private String color;
    private String engine;
    private String chassis;

    @DateCreated
    @Column(name = "date_created", updatable = false)
    private LocalDateTime dateCreated;

    @DateUpdated
    @Column(name = "date_updated")
    private LocalDateTime dateUpdated;
}
