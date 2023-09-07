package com.carcost.CarCost;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarCostRepository extends JpaRepository<CarData, Integer> {

    List<CarData> findAllByMakeContaining(String make);

    List<CarData> findAllByBody_typeContaining(String type);

    List<CarData> findAllByBody_typeContainingAndMakeContaining(String type, String make);

    List<CarData> findAllByMakeContainingOrderByPriceAsc(String make);

    List<CarData> findAllByMakeContainingAndModelContainingOrderByPriceAsc(String make, String model);

    List<CarData> findAllByMakeContainingAndModelContaining(String make, String model);
}