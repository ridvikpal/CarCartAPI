package com.carcost.CarCost;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CarCostRepository extends JpaRepository<CarData, Integer> {

    Optional<CarData> findCarDataByMake(String make);

    Optional<CarData> findCarDataByModel(String model);
}
