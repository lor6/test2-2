package com.baeldung.jpa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.baeldung.jpa.entity.Car;

@Repository
public interface CarRepository extends JpaRepository<Car, Integer> {

    @Procedure(procedureName = "GET_TOTAL_CARS_BY_MODEL")
    int getTotalCarsByModel(@Param("model_in") String model);

    @Query(value = "CALL FIND_CARS_AFTER_YEAR(:year_in);", nativeQuery = true)
    List<Car> findCarsAfterYear(@Param("year_in") Integer year_in);

}