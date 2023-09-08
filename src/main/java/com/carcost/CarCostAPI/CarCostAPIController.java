package com.carcost.CarCostAPI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "api/v1")
public class CarCostAPIController {
    private final CarCostAPIService carCostAPIService;

    @Autowired
    public CarCostAPIController(CarCostAPIService carCostAPIService) {
        this.carCostAPIService = carCostAPIService;
    }

    @GetMapping(path = "/search_make")
    public APIDataReturn getMakeListings(@RequestParam String make, @RequestParam(required = false) boolean low_price){
        return carCostAPIService.returnMakeListings(make, low_price);
    }

    @GetMapping(path = "/search_model")
    public APIDataReturn getModelListings(
            @RequestParam String make,
            @RequestParam String model,
            @RequestParam(required = false) boolean low_price
    ){
        return carCostAPIService.returnModelListings(make, model, low_price);
    }

    /* STILL IN DEVELOPMENT */
    @GetMapping(path = "/recommendation")
    public APIDataReturn getRecommendation(@RequestParam String type, @RequestParam(required = false) String make){
        return carCostAPIService.returnCarRecommendations(type, make);
    }
}