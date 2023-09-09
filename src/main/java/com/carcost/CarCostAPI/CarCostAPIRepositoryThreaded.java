package com.carcost.CarCostAPI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component
public class CarCostAPIRepositoryThreaded {

    private CarCostAPIRepository carCostAPIRepository;
    private Logger logger = LoggerFactory.getLogger(CarCostAPIRepositoryThreaded.class);

    @Autowired
    public CarCostAPIRepositoryThreaded(CarCostAPIRepository carCostAPIRepository) {
        this.carCostAPIRepository = carCostAPIRepository;
    }

    @SuppressWarnings("unused")
    @Async
    public CompletableFuture<List<CarData>> threadedFindAllByMakeContaining(String make) {
        logger.info("Starting thread for returning make listings from database on {}", Thread.currentThread().getName());
        long start = System.currentTimeMillis();
        List<CarData> result = carCostAPIRepository.findAllByMakeContaining(make);
        long end = System.currentTimeMillis();
        logger.info("Ending thread for returning make listings from database on {}", Thread.currentThread().getName());
        logger.info("Completed returning make listings from database in {}", (end - start));
        return CompletableFuture.completedFuture(result);
    }

    @SuppressWarnings("unused")
    @Async
    public CompletableFuture<List<CarData>> threadedFindAllByBodyTypeContaining(String type) {
        logger.info("Starting thread for returning type listings from database on {}", Thread.currentThread().getName());
        long start = System.currentTimeMillis();
        List<CarData> result = carCostAPIRepository.findAllByBodyTypeContaining(type);
        long end = System.currentTimeMillis();
        logger.info("Ending thread for returning type listings from database on {}", Thread.currentThread().getName());
        logger.info("Completed returning type listings from database in {}", (end - start));
        return CompletableFuture.completedFuture(result);
    }

    @SuppressWarnings("unused")
    @Async
    public CompletableFuture<List<CarData>> threadedFindAllByBodyTypeContainingAndMakeContaining(String type, String make) {
        logger.info("Starting thread for returning type listings with specific make from database on {}", Thread.currentThread().getName());
        long start = System.currentTimeMillis();
        List<CarData> result = carCostAPIRepository.findAllByBodyTypeContainingAndMakeContaining(type, make);
        long end = System.currentTimeMillis();
        logger.info("Ending thread for returning type listings with specific make from database on {}", Thread.currentThread().getName());
        logger.info("Completed returning type listings with specific make from database in {}", (end - start));
        return CompletableFuture.completedFuture(result);
    }

    @SuppressWarnings("unused")
    @Async
    public CompletableFuture<List<CarData>> threadedFindAllByMakeContainingOrderByPriceAsc(String make) {
        logger.info("Starting thread for returning make listings ordered by low price from database on {}", Thread.currentThread().getName());
        long start = System.currentTimeMillis();
        List<CarData> result = carCostAPIRepository.findAllByMakeContainingOrderByPriceAsc(make);
        long end = System.currentTimeMillis();
        logger.info("Ending thread for returning make listings ordered by low price from database on {}", Thread.currentThread().getName());
        logger.info("Completed returning make listings ordered by low price from database in {}", (end - start));
        return CompletableFuture.completedFuture(result);
    }

    @SuppressWarnings("unused")
    @Async
    public CompletableFuture<List<CarData>> threadedFindAllByMakeContainingAndModelContainingOrderByPriceAsc(String make, String model) {
        logger.info("Starting thread for returning model listings ordered by low price from database on {}", Thread.currentThread().getName());
        long start = System.currentTimeMillis();
        List<CarData> result = carCostAPIRepository.findAllByMakeContainingAndModelContainingOrderByPriceAsc(make, model);
        long end = System.currentTimeMillis();
        logger.info("Ending thread for returning model listings ordered by low price from database on {}", Thread.currentThread().getName());
        logger.info("Completed returning model listings ordered by low price from database in {}", (end - start));
        return CompletableFuture.completedFuture(result);
    }

    @SuppressWarnings("unused")
    @Async
    public CompletableFuture<List<CarData>> threadedFindAllByMakeContainingAndModelContaining(String make, String model) {
        logger.info("Starting thread for returning model listings from database on {}", Thread.currentThread().getName());
        long start = System.currentTimeMillis();
        List<CarData> result = carCostAPIRepository.findAllByMakeContainingAndModelContaining(make, model);
        long end = System.currentTimeMillis();
        logger.info("Ending thread for returning model listings from database on {}", Thread.currentThread().getName());
        logger.info("Completed returning model listings from database in {}", (end - start));
        return CompletableFuture.completedFuture(result);
    }
}
