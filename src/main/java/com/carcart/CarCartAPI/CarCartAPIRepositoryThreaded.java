package com.carcart.CarCartAPI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Component
public class CarCartAPIRepositoryThreaded {

    private CarCartAPIRepository carCartAPIRepository;
    private Logger logger = LoggerFactory.getLogger(CarCartAPIRepositoryThreaded.class);

    @Autowired
    public CarCartAPIRepositoryThreaded(CarCartAPIRepository carCartAPIRepository) {
        this.carCartAPIRepository = carCartAPIRepository;
    }

    
    @Async
    public CompletableFuture<List<CarData>> threadedFindAllByMakeContaining(String make) {
        logger.info("Starting thread for returning make listings from database on {}", Thread.currentThread().getName());
        long start = System.currentTimeMillis();
        List<CarData> result = carCartAPIRepository.findAllByMakeContaining(make);
        long end = System.currentTimeMillis();
        logger.info("Ending thread for returning make listings from database on {}", Thread.currentThread().getName());
        logger.info("Completed returning make listings from database in {} ms", (end - start));
        return CompletableFuture.completedFuture(result);
    }

    
    @Async
    public CompletableFuture<List<CarData>> threadedFindAllByBodyTypeContaining(String type) {
        logger.info("Starting thread for returning type listings from database on {}", Thread.currentThread().getName());
        long start = System.currentTimeMillis();
        List<CarData> result = carCartAPIRepository.findAllByBodyTypeContaining(type);
        long end = System.currentTimeMillis();
        logger.info("Ending thread for returning type listings from database on {}", Thread.currentThread().getName());
        logger.info("Completed returning type listings from database in {} ms", (end - start));
        return CompletableFuture.completedFuture(result);
    }

    
    @Async
    public CompletableFuture<List<CarData>> threadedFindAllByBodyTypeContainingAndMakeContaining(String type, String make) {
        logger.info("Starting thread for returning type listings with specific make from database on {}", Thread.currentThread().getName());
        long start = System.currentTimeMillis();
        List<CarData> result = carCartAPIRepository.findAllByBodyTypeContainingAndMakeContaining(type, make);
        long end = System.currentTimeMillis();
        logger.info("Ending thread for returning type listings with specific make from database on {}", Thread.currentThread().getName());
        logger.info("Completed returning type listings with specific make from database in {} ms", (end - start));
        return CompletableFuture.completedFuture(result);
    }

    
    @Async
    public CompletableFuture<List<CarData>> threadedFindAllByMakeContainingOrderByPriceAsc(String make) {
        logger.info("Starting thread for returning make listings ordered by low price from database on {}", Thread.currentThread().getName());
        long start = System.currentTimeMillis();
        List<CarData> result = carCartAPIRepository.findAllByMakeContainingOrderByPriceAsc(make);
        long end = System.currentTimeMillis();
        logger.info("Ending thread for returning make listings ordered by low price from database on {}", Thread.currentThread().getName());
        logger.info("Completed returning make listings ordered by low price from database in {} ms", (end - start));
        return CompletableFuture.completedFuture(result);
    }

    
    @Async
    public CompletableFuture<List<CarData>> threadedFindAllByMakeContainingAndModelContainingOrderByPriceAsc(String make, String model) {
        logger.info("Starting thread for returning model listings ordered by low price from database on {}", Thread.currentThread().getName());
        long start = System.currentTimeMillis();
        List<CarData> result = carCartAPIRepository.findAllByMakeContainingAndModelContainingOrderByPriceAsc(make, model);
        long end = System.currentTimeMillis();
        logger.info("Ending thread for returning model listings ordered by low price from database on {}", Thread.currentThread().getName());
        logger.info("Completed returning model listings ordered by low price from database in {} ms", (end - start));
        return CompletableFuture.completedFuture(result);
    }
    
    @Async
    public CompletableFuture<List<CarData>> threadedFindAllByMakeContainingAndModelContaining(String make, String model) {
        logger.info("Starting thread for returning model listings from database on {}", Thread.currentThread().getName());
        long start = System.currentTimeMillis();
        List<CarData> result = carCartAPIRepository.findAllByMakeContainingAndModelContaining(make, model);
        long end = System.currentTimeMillis();
        logger.info("Ending thread for returning model listings from database on {}", Thread.currentThread().getName());
        logger.info("Completed returning model listings from database in {} ms", (end - start));
        return CompletableFuture.completedFuture(result);
    }

    @Async
    public CompletableFuture<CarData> threadedInsertIntoDatabase(CarData carData){
        logger.info("Starting thread for adding new car listing to database on {}", Thread.currentThread().getName());
        long start = System.currentTimeMillis();
        boolean alreadyExists = carCartAPIRepository.existsById(carData.getId());
        if (alreadyExists){
            long end = System.currentTimeMillis();
            logger.info("Ending thread for adding new car listing to database on {}", Thread.currentThread().getName());
            logger.info("Completed returning adding new car listing to database in {} ms", (end - start));
            return null;
        }
        CarData newEntry = carCartAPIRepository.save(carData);
        long end = System.currentTimeMillis();
        logger.info("Ending thread for adding new car listing to database on {}", Thread.currentThread().getName());
        logger.info("Completed returning adding new car listing to database in {} ms", (end - start));
        return CompletableFuture.completedFuture(newEntry);
    }

    @Async
    public CompletableFuture<CarData> threadedUpdateInDatabase(int id, String price, String miles, String sellerName, String street, String city, String state, String zip) {
        logger.info("Starting thread for updating car listing in database on {}", Thread.currentThread().getName());
        long start = System.currentTimeMillis();
        CarData carData = carCartAPIRepository.findById(id).get();
        if (price != null && !(price.trim().isEmpty())){
            carData.setPrice(price);
        }
        if (miles != null && !(miles.trim().isEmpty())){
            carData.setMiles(miles);
        }
        if (sellerName != null && !(sellerName.trim().isEmpty())){
            carData.setSellerName(sellerName);
        }
        if (street != null && !(street.trim().isEmpty())){
            carData.setStreet(street);
        }
        if (city != null && !(city.trim().isEmpty())){
            carData.setCity(city);
        }
        if (state != null && !(state.trim().isEmpty())){
            carData.setState(state);
        }
        if (zip != null && !(zip.trim().isEmpty())){
            carData.setZip(zip);
        }

        carCartAPIRepository.save(carData);

        long end = System.currentTimeMillis();
        logger.info("Ending thread for updating car listing in database on {}", Thread.currentThread().getName());
        logger.info("Completed updating car listing in database in {} ms", (end - start));
        return CompletableFuture.completedFuture(carData);
    }
    
    @Async
    public CompletableFuture<CarData> threadedDeleteFromDatabase(int id){
        logger.info("Starting thread for deleting car listing in database on {}", Thread.currentThread().getName());
        long start = System.currentTimeMillis();
        Optional<CarData> carDataOptional = carCartAPIRepository.findById(id);

        CarData carDataCopy = carDataOptional.get();
        carCartAPIRepository.delete(carDataOptional.get());

        long end = System.currentTimeMillis();
        logger.info("Ending thread for deleting car listing in database on {}", Thread.currentThread().getName());
        logger.info("Completed deleting car listing in database in {} ms", (end - start));
        return CompletableFuture.completedFuture(carDataCopy);
    }
}
