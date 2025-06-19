package com.jumpcar.domain.repository;

import com.jumpcar.domain.model.entity.Car;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {

}
