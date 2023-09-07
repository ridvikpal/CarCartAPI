package com.carcost.CarCost;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarCostRepository extends JpaRepository<CarData, Integer> {

    List<CarData> findAllByMakeContaining(String make);

    List<CarData> findAllByMakeContainingOrderByPriceAsc(String make);

    List<CarData> findAllByMakeContainingAndModelContaining(String make, String model);
}
