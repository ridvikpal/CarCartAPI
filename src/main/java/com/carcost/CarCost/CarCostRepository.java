package com.carcost.CarCost;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CarCostRepository extends JpaRepository<CarData, Integer> {

    List<CarData> findAllByMake(String make);

    List<CarData> findAllByMakeAndModel(String make, String model);
}
