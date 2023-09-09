package com.carcost.CarCostAPI;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Repository
public interface CarCostAPIRepository extends JpaRepository<CarData, Integer> {

    // Standard JPA Repository Functions
    List<CarData> findAllByMakeContaining(String make);

    List<CarData> findAllByBodyTypeContaining(String type);

    List<CarData> findAllByBodyTypeContainingAndMakeContaining(String type, String make);

    List<CarData> findAllByMakeContainingOrderByPriceAsc(String make);

    List<CarData> findAllByMakeContainingAndModelContainingOrderByPriceAsc(String make, String model);

    List<CarData> findAllByMakeContainingAndModelContaining(String make, String model);

    // Threaded versions of the JPA Repository Function
//    public CompletableFuture<List<CarData>> threadedFindAllByMakeContaining(String make);
//    public CompletableFuture<List<CarData>> threadedFindAllByBodyTypeContaining(String type);
//    public CompletableFuture<List<CarData>> threadedFindAllByBodyTypeContainingAndMakeContaining(String type, String make);
//    public CompletableFuture<List<CarData>> threadedFindAllByMakeContainingOrderByPriceAsc(String make);
//    public CompletableFuture<List<CarData>> threadedFindAllByMakeContainingAndModelContainingOrderByPriceAsc(String make, String model);
//    public CompletableFuture<List<CarData>> threadedFindAllByMakeContainingAndModelContaining(String make, String model);
}
