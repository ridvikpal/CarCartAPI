package com.carcost.CarCostAPI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping(path = "carcostapi")
public class CarCostAPIController {
    private final CarCostAPIService carCostAPIService;

    Logger logger = LoggerFactory.getLogger(CarCostAPIController.class);

    @Autowired
    public CarCostAPIController(CarCostAPIService carCostAPIService) {
        this.carCostAPIService = carCostAPIService;
    }

    @GetMapping(path = "/search_make")
    public CompletableFuture<APIDataReturn> getMakeListings(@RequestParam String make, @RequestParam(required = false) boolean low_price){
        try {
            CompletableFuture<APIDataReturn> apiDataReturnCompletableFuture = carCostAPIService.returnMakeListings(make, low_price);
            return apiDataReturnCompletableFuture;
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping(path = "/search_model")
    public CompletableFuture<APIDataReturn> getModelListings(
            @RequestParam String make,
            @RequestParam String model,
            @RequestParam(required = false) boolean low_price
    ){
        try {
            CompletableFuture<APIDataReturn> apiDataReturnCompletableFuture = carCostAPIService.returnModelListings(make, model, low_price);
            return apiDataReturnCompletableFuture;
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping(path = "/recommendation")
    public CompletableFuture<APIDataReturn> getRecommendation(@RequestParam String type, @RequestParam(required = false) String make){
        try {
            CompletableFuture<APIDataReturn> apiDataReturnCompletableFuture = carCostAPIService.returnCarRecommendations(type, make);
            return apiDataReturnCompletableFuture;
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
