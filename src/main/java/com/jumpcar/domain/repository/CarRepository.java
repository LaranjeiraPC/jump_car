package com.jumpcar.domain.repository;

import com.jumpcar.domain.model.entity.Car;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;

import java.util.Optional;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {

    Optional<Car> findByChassisContainsIgnoreCase(String chassis);

    void deleteByChassisContainsIgnoreCase(String chassis);

    Page<Car> findAll(Pageable pageable);

}
